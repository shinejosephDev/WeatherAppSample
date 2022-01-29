package com.shinejoseph.weathersample.data.remote.dto

import com.shinejoseph.weathersample.domain.model.Weather
import com.shinejoseph.weathersample.presentation.toDateTimeString

data class WeatherDto(
    val current: CurrentDto,
    val daily: List<DailyDto>
)


fun WeatherDto.toWeather(): List<Weather> {
    val list = mutableListOf<Weather>()
    list.add(
        Weather(
            date = "Today",
            temp = current.temp,
            icon = current.weather[0].icon,
            pressure = current.pressure,
            humidity = current.humidity,
            wind_speed = current.wind_speed
        )
    )

    daily.forEach {
        list.add(
            Weather(
                date = it.dt.toDateTimeString(format = "DD MMM"),
                temp = it.temp.day,
                icon = it.weather[0].icon,
                pressure = it.pressure,
                humidity = it.humidity,
                wind_speed = it.wind_speed
            )
        )
    }
    return list
}