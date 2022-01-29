package com.shinejoseph.weathersample.data.remote.dto

data class DailyDto(
    val dt: Double,
    val temp: TempDto,
    val pressure: Double,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<IconDto>
)
