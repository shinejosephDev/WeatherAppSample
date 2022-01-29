package com.shinejoseph.weathersample.di

import android.content.Context
import androidx.room.Room
import com.shinejoseph.weathersample.data.local.CityDao
import com.shinejoseph.weathersample.data.local.CurrentWeatherDao
import com.shinejoseph.weathersample.data.local.WeatherDatabase
import com.shinejoseph.weathersample.presentation.MainApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext appContext: Context): WeatherDatabase {
        return Room
            .databaseBuilder(appContext, WeatherDatabase::class.java, WeatherDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCityDao(db: WeatherDatabase): CityDao {
        return db.getCityDao
    }

    @Provides
    fun provideWeatherDao(db: WeatherDatabase): CurrentWeatherDao {
        return db.getCurrentWeatherDao
    }
}