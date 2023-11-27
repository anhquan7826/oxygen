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
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.nhom1.oxygen.repository.HistoryRepository
import com.nhom1.oxygen.repository.LocationRepository
import com.nhom1.oxygen.repository.UserRepository
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.utils.constants.SPKeys
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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var historyRepository: HistoryRepository

    private val minTimeInterval = 300000L


    override fun onCreate() {
        super.onCreate()
        sharedPreferences = applicationContext.getSharedPreferences(
            applicationContext.packageName, Context.MODE_PRIVATE
        )
        locationManager = applicationContext.getSystemService(LocationManager::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        configureNotificationChannel()
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        infoLog("${this::class.simpleName}: Started.")
        startForeground(1, buildPersistentNotification())
        trackLocation()
        return START_STICKY
    }

    private fun configureNotificationChannel() {
        val notificationChannel = NotificationChannel(
            "$packageName.service", "Persistent Notification", NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        getSystemService(NotificationManager::class.java).createNotificationChannel(
            notificationChannel
        )
    }

    private fun buildPersistentNotification(): Notification {
        return Notification.Builder(this, "$packageName.service").setOngoing(true)
            .setContentTitle("Oxygen").setContentText("Oxygen is running in the background.")
            .setContentIntent(
                PendingIntent.getActivity(
                    this, 0, Intent(this, HomeActivity::class.java), PendingIntent.FLAG_IMMUTABLE
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
        }
        addHistory()
    }

    private var prevHour = -1
    private fun addHistory() {
        if (userRepository.isSignedIn()) {
            val hour = getHour(now())
            if (prevHour < hour) {
                historyRepository.addLocationHistory()
                prevHour = hour
            }
        }
    }
}