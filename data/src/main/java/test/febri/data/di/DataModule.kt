package test.febri.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import test.febri.data.remote.ApiService
import test.febri.data.remote.RemoteDataSource
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideSpaceNewsApi(@ApplicationContext context: Context): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.spaceflightnewsapi.net")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGithubRemoteDataSource(
        apiService: ApiService
    ): RemoteDataSource  = RemoteDataSource(apiService)
}