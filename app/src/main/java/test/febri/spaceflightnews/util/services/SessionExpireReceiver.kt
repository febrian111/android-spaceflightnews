package test.febri.spaceflightnews.util.services

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import test.febri.spaceflightnews.util.UserSessionManager
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SessionExpireReceiver : BroadcastReceiver() {

    @Inject
    lateinit var sessionManager: UserSessionManager

    @Inject
    lateinit var sessionExpireAlarm: SessionExpireAlarm

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            if (sessionManager.isSessionExpired()) {
                Timber.d("SessionExpireReceiver", "Session expired. Logging out user.")

                // Show push notification
                showSessionExpiredNotification(it)

                // Send broadcast to force logout
                val logoutIntent = Intent("ACTION_FORCE_LOGOUT")
                it.sendBroadcast(logoutIntent)

                // Clear session
                sessionManager.clearSession()
            } else {
                // Reschedule alarm in case the app restarted
                sessionExpireAlarm.scheduleSessionExpiration()
            }
        }
    }

    private fun showSessionExpiredNotification(context: Context) {
        val channelId = "session_expired_channel"

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Session Expired")
            .setContentText("Your session has expired. Please log in again.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .build()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify(2, notification)
        }
    }
}
