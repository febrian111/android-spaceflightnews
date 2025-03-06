package test.febri.spaceflightnews.di


import android.content.Context
import com.auth0.android.Auth0
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import test.febri.spaceflightnews.util.UserSessionManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): UserSessionManager {
        return UserSessionManager(context)
    }

    @Provides
    @Singleton
    fun provideAuth0Account(@ApplicationContext context: Context): Auth0 {
        return Auth0("dGhxrJ0lZz7Un6YEXUtS6DcMfo1XrRXa", "dev-xe3xg2qingzvgv8.us.auth0.com")
    }

//    @Provides
//    @Singleton
//    fun provideSessionExpireAlarm(
//        @ApplicationContext context: Context,
//        sessionManager: UserSessionManager
//    ): SessionExpireAlarm {
//        return SessionExpireAlarm(context, sessionManager)
//    }
}
