package com.nhom1.oxygen

import android.Manifest
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.nhom1.oxygen.data.model.notification.ONotification
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.NotificationRepository
import com.nhom1.oxygen.repository.SettingRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.utils.NotificationUtil
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.getAQILevel
import com.nhom1.oxygen.utils.getHour
import com.nhom1.oxygen.utils.gson
import com.nhom1.oxygen.utils.infoLog
import com.nhom1.oxygen.utils.listen
import com.nhom1.oxygen.utils.now
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MainService : Service() {
    companion object {
        fun startService(context: Context) {
            context.startForegroundService(Intent(context, MainService::class.java))
        }

        fun stopService(context: Context) {
            context.stopService(Intent(context, MainService::class.java))
        }
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var locationManager: LocationManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var historyRepository: HistoryRepository

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var settingsRepository: SettingRepository

    @Inject
    lateinit var notificationRepository: NotificationRepository

    private val minTimeInterval = 300 * 1000L

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(
            packageName, Context.MODE_PRIVATE
        )
        locationManager = getSystemService(LocationManager::class.java)
        notificationManager = getSystemService(NotificationManager::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        NotificationUtil.configure(applicationContext)
        startForeground(1, NotificationUtil.getPersistentNotification(applicationContext))
        trackLocation()
        infoLog("${this::class.simpleName}: Created.")
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        infoLog("${this::class.simpleName}: Started.")
        prevHour = sharedPreferences.getLong(SPKeys.Cache.CACHE_UPLOAD_HISTORY_TIMESTAMP, -1)
        updateWeatherInfo()
        return START_STICKY
    }

    private fun trackLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        infoLog("${this::class.simpleName}: Tracking user's location: ${LocalDateTime.now()}.")
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            try {
                onLocationUpdate(it.latitude, it.longitude)
            } catch (_: Exception) {}
        }
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, minTimeInterval).build(),
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let {
                        onLocationUpdate(it.latitude, it.longitude)
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun onLocationUpdate(latitude: Double?, longitude: Double?) {
        if (latitude == null || longitude == null) return
        infoLog("${this::class.simpleName}: User's location updated: $latitude, $longitude: ${LocalDateTime.now()}")
        locationRepository.getLocationFromCoordinate(latitude, longitude).listen {
            infoLog("${this::class.simpleName}: Got location info: $it: ${LocalDateTime.now()}")
            sharedPreferences
                .edit()
                .putString(SPKeys.CURRENT_LOCATION, gson.toJson(it))
                .apply()
            addHistory()
            updateWeatherInfo()
        }
    }

    private var prevHour = -1L


    private fun addHistory() {
        if (userRepository.isSignedIn()) {
            if (now() - prevHour > 3600) {
                infoLog("${this::class.simpleName}: Adding user's history.")
                historyRepository.addLocationHistory().listen(
                    onError = {
                        Toast.makeText(
                            this,
                            "Cannot upload history. ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    prevHour = now()
                    sharedPreferences.edit {
                        putLong(SPKeys.Cache.CACHE_UPLOAD_HISTORY_TIMESTAMP, prevHour)
                    }
                }
            }
        }
    }

    private fun updateWeatherInfo() {
        locationRepository.getCurrentLocation().listen { location ->
            weatherRepository.getCurrentWeatherInfo(location).listen { weather ->
                NotificationUtil.updatePersistentNotification(
                    applicationContext,
                    weather,
                    settingsRepository.temperatureUnit
                )
                showWarningNotification(weather.airQuality.aqi)
            }
        }
    }

    private var previousAQILevel: Int = Int.MAX_VALUE
    private fun showWarningNotification(aqi: Int) {
        if (!settingsRepository.receiveNotification) return
        val currentAQILevel = getAQILevel(aqi)
        if (currentAQILevel > previousAQILevel) {
            NotificationUtil.showWarningNotification(applicationContext)
            notificationRepository.addNotification(
                ONotification(
                    type = ONotification.TYPE_WARNING,
                    time = now(),
                    message = getString(R.string.you_are_entering_polluted_area_please_take_cautious)
                )
            ).listen {}
        }
        previousAQILevel = currentAQILevel
    }
}