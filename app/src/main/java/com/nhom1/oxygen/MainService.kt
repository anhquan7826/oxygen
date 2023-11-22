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
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.utils.constants.SPKeys
import com.nhom1.oxygen.utils.errorLog

class MainService : Service() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var locationManager: LocationManager

    private val minTimeInterval = 300000L
    private val minDistance = 10F


    override fun onCreate() {
        super.onCreate()
        sharedPreferences = applicationContext.getSharedPreferences(
            applicationContext.packageName, Context.MODE_PRIVATE
        )
        locationManager = applicationContext.getSystemService(LocationManager::class.java)
        configureNotificationChannel()
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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

    private fun getCurrentLocation() {
        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider.lastLocation.addOnSuccessListener {
                onLocationUpdate(it)
            }.addOnFailureListener {
                errorLog("${this::class.simpleName}: Cannot get current location.")
            }
        }
    }

    private fun trackLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                locationManager.requestLocationUpdates(
                    LocationManager.FUSED_PROVIDER, minTimeInterval, minDistance
                ) {
                    onLocationUpdate(it)
                }
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, minTimeInterval, minDistance
                ) {
                    onLocationUpdate(it)
                }
            }
        }
    }

    private fun onLocationUpdate(location: Location) {
        sharedPreferences.edit().putFloat(SPKeys.CURRENT_LAT, location.latitude.toFloat())
            .putFloat(SPKeys.CURRENT_LON, location.longitude.toFloat()).apply()
    }
}