package com.nhom1.oxygen

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.IBinder
import android.os.Looper
import android.widget.RemoteViews
import android.widget.Toast
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.nhom1.oxygen.common.constants.getAQIColor
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.SettingRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.repository.WeatherRepository
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.extensions.toPrettyString
import com.nhom1.oxygen.utils.getHour
import com.nhom1.oxygen.utils.infoLog
import com.nhom1.oxygen.utils.listen
import com.nhom1.oxygen.utils.now
import com.nhom1.oxygen.utils.toJson
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MainService : Service() {
    companion object {
        fun startService(context: Context) {
            context.stopService(Intent(context, MainService::class.java))
            context.startForegroundService(Intent(context, MainService::class.java))
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

    private lateinit var channelId: String
    private val minTimeInterval = 300000L

    private lateinit var notification: Notification
    private lateinit var notificationView: RemoteViews

    override fun onCreate() {
        super.onCreate()
        channelId = "$packageName.service"
        sharedPreferences = getSharedPreferences(
            packageName, Context.MODE_PRIVATE
        )
        locationManager = getSystemService(LocationManager::class.java)
        notificationManager = getSystemService(NotificationManager::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        notificationView = RemoteViews(packageName, R.layout.layout_notification_expanded)
        configureNotificationChannel()
        notification = buildPersistentNotification()
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        infoLog("${this::class.simpleName}: Started.")
        startForeground(1, notification)
        trackLocation()
        return START_STICKY
    }

    private fun configureNotificationChannel() {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                channelId,
                "Persistent Notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This channel is used for Oxygen background service."
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
        )
    }

    private fun buildPersistentNotification(): Notification {
        return Notification.Builder(this, "$packageName.service")
            .setSmallIcon(R.drawable.fresh_air_rounded)
            .setCustomContentView(RemoteViews(packageName, R.layout.layout_notification))
            .setCustomBigContentView(notificationView)
            .setOngoing(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, HomeActivity::class.java).putExtra("refresh", true),
                    PendingIntent.FLAG_IMMUTABLE
                )
            ).build()
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

    private fun onLocationUpdate(latitude: Double, longitude: Double) {
        infoLog("${this::class.simpleName}: User's location updated: $latitude, $longitude: ${LocalDateTime.now()}")
        locationRepository.getLocationFromCoordinate(latitude, longitude).listen {
            sharedPreferences
                .edit()
                .putString(SPKeys.CURRENT_LOCATION, toJson(it))
                .apply()
            addHistory()
            updateWeatherInfo()
        }
    }

    private var prevHour = -1
    private fun addHistory() {
        infoLog("${this::class.simpleName}: Adding user's history.")
        if (userRepository.isSignedIn()) {
            val hour = getHour(now())
            if (prevHour < hour) {
                historyRepository.addLocationHistory().listen(
                    onError = {
                        Toast.makeText(
                            this,
                            "Cannot upload history. ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    prevHour = hour
                }
            }
        }
    }

    private fun updateWeatherInfo() {
        infoLog("${this::class.simpleName}: Getting weather info.")
        locationRepository.getCurrentLocation().listen { location ->
            weatherRepository.getCurrentWeatherInfo(location).listen { weather ->
                infoLog("${this::class.simpleName}: Setting weather info.")
                notificationView.apply {
                    weather.airQuality.aqi.let {
                        setTextViewText(R.id.aqi, it.toString())
                        setTextColor(R.id.aqi, getAQIColor(it).toArgb())
                        setTextViewText(
                            R.id.aqi_desc,
                            getString(
                                when {
                                    (it in 0..50) -> R.string.good
                                    (it in 51..100) -> R.string.moderate
                                    (it in 101..150) -> R.string.bad
                                    (it in 151..200) -> R.string.unhealthy
                                    (it in 201..300) -> R.string.very_unhealthy
                                    else -> R.string.hazardous
                                }
                            )
                        )
                    }

                    val target = NotificationTarget(
                        this@MainService,
                        R.id.weather,
                        notificationView,
                        notification,
                        1
                    )
                    Glide
                        .with(this@MainService)
                        .asBitmap()
                        .load(Uri.parse(weather.condition.icon))
                        .into(target)
                    setTextViewText(
                        R.id.temperature,
                        if (settingsRepository.temperatureUnit) "${weather.tempC.toPrettyString()}°C" else "${weather.tempF.toPrettyString()}°F"
                    )
                }
                notificationManager.notify(1, notification)
            }
        }
    }
}