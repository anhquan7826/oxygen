package com.nhom1.oxygen.repository

import com.nhom1.oxygen.data.model.notification.ONotification
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface NotificationRepository {
    fun getAllNotifications(): Single<List<ONotification>>

    fun countNotifications(): Single<Int>

    fun addNotification(notification: ONotification): Completable

    fun deleteNotification(notification: ONotification): Completable

    fun deleteAllNotifications(): Completable
}