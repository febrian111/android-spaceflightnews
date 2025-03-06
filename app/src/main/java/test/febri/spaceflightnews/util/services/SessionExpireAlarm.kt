package test.febri.spaceflightnews.util.services

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import test.febri.spaceflightnews.util.UserSessionManager
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionExpireAlarm @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionManager: UserSessionManager
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleSessionExpiration() {
        val intent = Intent(context, SessionExpireReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set expiration time in SharedPreferences
        val expirationTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2)
        sessionManager.setSessionExpiration(expirationTime)

        // Schedule alarm
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                // Android 12+ (API 31): Need special permission for exact alarms
                if (alarmManager.canScheduleExactAlarms()) {
                    Timber.d("SessionExpireAlarm", "Scheduling exact alarm on API 31+")
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, expirationTime, pendingIntent)
                } else {
                    Timber.w("SessionExpireAlarm", "Exact alarm permission not granted. Requesting permission.")
                    requestExactAlarmPermission()
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                // Android 6+ (API 23+): Use `setExactAndAllowWhileIdle()`
                Timber.d("SessionExpireAlarm", "Scheduling exact alarm on API 23-30")
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, expirationTime, pendingIntent)
            }
            else -> {
                // Android 5 (API 21-22): No `setExactAndAllowWhileIdle()`, fallback to `setExact()`
                Timber.d("SessionExpireAlarm", "Scheduling exact alarm on API 21-22")
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, expirationTime, pendingIntent)
            }
//            else -> {
//                // Fallback for older devices
//                Timber.d("SessionExpireAlarm", "Scheduling normal alarm on API <21")
//                alarmManager.set(AlarmManager.RTC_WAKEUP, expirationTime, pendingIntent)
//            }
        }
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlertDialog.Builder(context)
                .setTitle("Allow Exact Alarms")
                .setMessage("To ensure proper session expiration, please allow exact alarms in settings.")
                .setPositiveButton("Go to Settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    context.startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    fun cancelSessionExpiration() {
        val intent = Intent(context, SessionExpireReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        sessionManager.clearSession()
    }
}
