package test.febri.spaceflightnews.util.services

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import test.febri.spaceflightnews.R
import test.febri.spaceflightnews.login.LoginActivity
import test.febri.spaceflightnews.util.UserSessionManager
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SessionExpireReceiver : BroadcastReceiver() {

    @Inject
    lateinit var sessionManager: UserSessionManager

    @Inject
    lateinit var sessionExpireAlarm: SessionExpireAlarm

    @Inject
    lateinit var accountAuth0: Auth0

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
                logout(it)
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



    private fun logout(context: Context) {
        WebAuthProvider.logout(accountAuth0)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    // The user has been logged out!
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }

                override fun onFailure(exception: AuthenticationException) {
//                    updateUI()
                    Toast.makeText(context, "Failure: ${exception.getCode()}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
