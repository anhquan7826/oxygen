package com.nhom1.oxygen.ui.notification

import androidx.lifecycle.ViewModel
import com.nhom1.oxygen.data.model.notification.ONotification
import com.nhom1.oxygen.repository.NotificationRepository
import com.nhom1.oxygen.utils.constants.LoadState
import com.nhom1.oxygen.utils.listen
import com.nhom1.oxygen.utils.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    data class NotificationState(
        val state: LoadState = LoadState.LOADING,
        val notifications: List<ONotification> = listOf(),
        val error: String? = null,
    )

    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update {
            NotificationState()
        }
        notificationRepository.getAllNotifications().listen(
            onError = { exception ->
                _state.update {
                    NotificationState(
                        state = LoadState.ERROR,
                        error = exception.message
                    )
                }
            }
        ) { result ->
            _state.update {
                NotificationState(
                    state = LoadState.LOADED,
                    notifications = result
                )
            }
        }
    }

    fun addNotification() {
        val notification = ONotification(
            id = Random.nextInt(),
            type = ONotification.TYPE_SUGGESTION,
            time = now(),
            message = "Sample notification"
        )
        notificationRepository.addNotification(notification).listen {
            _state.update {
                it.copy(
                    notifications = it.notifications.plusElement(notification)
                )
            }
        }
    }

    fun clearNotification(notification: ONotification) {
        notificationRepository.deleteNotification(notification).listen {
            _state.update {
                it.copy(
                    notifications = it.notifications.minus(notification)
                )
            }
        }
    }

    fun clearAllNotifications() {
        notificationRepository.deleteAllNotifications().listen {
            _state.update {
                it.copy(
                    notifications = listOf()
                )
            }
        }
    }
}