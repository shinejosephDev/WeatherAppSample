package com.shinejoseph.weathersample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val date: String,
    val temp: Double,
    val icon: String,
    val pressure: Double,
    val humidity: Int,
    val wind_speed: Double,
)
