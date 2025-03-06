package test.febri.spaceflightnews.di


import android.content.Context
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

//    @Provides
//    @Singleton
//    fun provideSessionExpireAlarm(
//        @ApplicationContext context: Context,
//        sessionManager: UserSessionManager
//    ): SessionExpireAlarm {
//        return SessionExpireAlarm(context, sessionManager)
//    }
}
