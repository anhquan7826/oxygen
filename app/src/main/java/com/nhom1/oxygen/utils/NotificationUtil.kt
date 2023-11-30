package com.nhom1.oxygen.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import com.nhom1.oxygen.MainActivity
import com.nhom1.oxygen.R
import com.nhom1.oxygen.data.model.weather.OWeather
import com.nhom1.oxygen.ui.home.HomeActivity
import com.nhom1.oxygen.utils.extensions.toPrettyString

object NotificationUtil {
    private lateinit var CHANNEL_ID_PERSISTENCE: String
    private lateinit var CHANNEL_ID_WARNING_NOTIFICATION: String

    private const val NOTIFICATION_ID_PERSISTENCE = 1
    private const val NOTIFICATION_ID_WARNING = 2

    private lateinit var persistentNotification: Notification
    private lateinit var persistentNotificationView: RemoteViews

    private lateinit var warningNotification: Notification

    fun configure(context: Context) {
        CHANNEL_ID_PERSISTENCE = "${context.packageName}.channel_persistence"
        CHANNEL_ID_WARNING_NOTIFICATION = "${context.packageName}.channel_warning_notification"
        context.getSystemService(NotificationManager::class.java).apply {
            createNotificationChannels(listOf(
                NotificationChannel(
                    CHANNEL_ID_PERSISTENCE,
                    "Persistent Notification",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "This channel is used for Oxygen background service."
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                },
                NotificationChannel(
                    CHANNEL_ID_WARNING_NOTIFICATION,
                    "Warning Notification",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "This channel is used for air quality warning."
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
            ))
        }
    }

    fun getPersistentNotification(context: Context): Notification {
        if (!this::persistentNotification.isInitialized) {
            persistentNotificationView = RemoteViews(
                context.packageName,
                R.layout.layout_notification_expanded
            )
            persistentNotification = buildNotification(
                context,
                CHANNEL_ID_PERSISTENCE,
                view = RemoteViews(
                        context.packageName,
                        R.layout.layout_notification
                    ),
                expandedView = persistentNotificationView,
                ongoing = true,
                requestCode = 0,
                intentExtras = Bundle().apply {
                    putBoolean("refresh", true)
                }
            )
        }
        return persistentNotification
    }

    fun updatePersistentNotification(context: Context, weather: OWeather, celsius: Boolean) {
        if (!this::persistentNotification.isInitialized) getPersistentNotification(context)
        persistentNotificationView.apply {
            weather.airQuality.aqi.let {
                setTextViewText(R.id.aqi, it.toString())
                setTextColor(R.id.aqi, getAQIColor(it).toArgb())
                setTextViewText(
                    R.id.aqi_desc,
                    context.getString(
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
                context,
                R.id.weather,
                persistentNotificationView,
                persistentNotification,
                1
            )
            Glide
                .with(context)
                .asBitmap()
                .load(Uri.parse(weather.condition.icon))
                .into(target)
            setTextViewText(
                R.id.temperature,
                if (celsius) "${weather.tempC.toPrettyString()}°C" else "${weather.tempF.toPrettyString()}°F"
            )
        }
        context.getSystemService(NotificationManager::class.java)
            .notify(NOTIFICATION_ID_PERSISTENCE, persistentNotification)
    }

    fun showWarningNotification(context: Context) {
        if (!this::warningNotification.isInitialized) {
            warningNotification = buildNotification(
                context,
                CHANNEL_ID_WARNING_NOTIFICATION,
                title = context.getString(R.string.warning),
                content = context.getString(R.string.warning_notification_content),
                requestCode = 1,
                intentExtras = Bundle().apply {
                    putBoolean("refresh", true)
                    putBoolean("goToSuggestion", true)
                }
            )
        }
        context.getSystemService(NotificationManager::class.java).notify(
            NOTIFICATION_ID_WARNING,
            warningNotification
        )
    }

    private fun buildNotification(
        context: Context,
        channel: String,
        title: String? = null,
        content: String? = null,
        view: RemoteViews? = null,
        expandedView: RemoteViews? = null,
        ongoing: Boolean = false,
        requestCode: Int,
        intentExtras: Bundle? = null
    ): Notification {
        return Notification.Builder(context, channel).apply {
            setSmallIcon(R.drawable.fresh_air_rounded)
            if (title != null) setContentTitle(title)
            if (content != null) setContentText(content)
            if (view != null) setCustomContentView(view)
            if (expandedView != null) setCustomBigContentView(expandedView)
            setOngoing(ongoing)
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    requestCode,
                    Intent(context, MainActivity::class.java).apply {
                        if (intentExtras != null) putExtras(intentExtras)
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
        }.build()
    }
}