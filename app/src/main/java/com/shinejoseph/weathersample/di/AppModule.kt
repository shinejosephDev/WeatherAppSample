package com.shinejoseph.weathersample.di

import com.shinejoseph.weathersample.common.Constants
import com.shinejoseph.weathersample.data.local.AppPrefs
import com.shinejoseph.weathersample.data.local.PrefsHelper
import com.shinejoseph.weathersample.data.remote.OpenWeatherApi
import com.shinejoseph.weathersample.data.repository.PrefRepositoryImpl
import com.shinejoseph.weathersample.data.repository.WeatherRepositoryImpl
import com.shinejoseph.weathersample.domain.repository.PrefRepository
import com.shinejoseph.weathersample.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder().addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(OpenWeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: OpenWeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePrefHelper(appPrefs: AppPrefs): PrefsHelper {
        return appPrefs
    }

    @Provides
    @Singleton
    fun providerUnitRepository(prefRepositoryImpl: PrefRepositoryImpl): PrefRepository =
        prefRepositoryImpl
}