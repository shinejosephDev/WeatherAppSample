package com.shinejoseph.weathersample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shinejoseph.weathersample.data.model.City
import com.shinejoseph.weathersample.data.model.CurrentWeather

@Database(entities = [City::class, CurrentWeather::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val getCityDao: CityDao

    abstract val getCurrentWeatherDao: CurrentWeatherDao

    companion object {
        const val DATABASE_NAME = "weather_db"
    }
}