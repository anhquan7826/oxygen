package com.nhom1.oxygen.repository.impl

import com.nhom1.oxygen.data.database.OxygenDatabase
import com.nhom1.oxygen.data.model.notification.ONotification
import com.nhom1.oxygen.repository.NotificationRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class NotificationRepositoryImpl(
    private val database: OxygenDatabase
) : NotificationRepository {
    override fun getAllNotifications(): Single<List<ONotification>> {
        return database.notificationDao().getAllNotifications()
    }

    override fun countNotifications(): Single<Int> {
        return database.notificationDao().countNotifications()
    }

    override fun addNotification(notification: ONotification): Completable {
        return database.notificationDao().addNotification(notification)
    }

    override fun deleteNotification(notification: ONotification): Completable {
        return database.notificationDao().deleteNotification(notification)
    }

    override fun deleteAllNotifications(): Completable {
        return database.notificationDao().deleteAllNotifications()
    }
}