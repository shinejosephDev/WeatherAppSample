package com.shinejoseph.weathersample.domain.model

import com.shinejoseph.weathersample.data.model.CurrentWeather

data class Weather(
    val date: String,
    val temp: Double,
    val icon: String,
    val pressure: Double,
    val humidity: Int,
    val wind_speed: Double,
)

fun Weather.toWeatherEntity(): CurrentWeather {
    return CurrentWeather(
        date = date,
        temp = temp,
        icon = icon,
        pressure = pressure,
        humidity = humidity, wind_speed = wind_speed
    )
}