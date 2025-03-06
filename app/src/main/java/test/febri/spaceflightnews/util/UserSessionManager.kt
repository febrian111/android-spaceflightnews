package test.febri.spaceflightnews.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun setSessionExpiration(timeInMillis: Long) {
        prefs.edit().putLong("session_expiration", timeInMillis).apply()
    }

    fun getSessionExpiration(): Long {
        return prefs.getLong("session_expiration", 0L)
    }

    fun isSessionExpired(): Boolean {
        return System.currentTimeMillis() >= getSessionExpiration()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}