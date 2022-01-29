package com.shinejoseph.weathersample.data.remote.dto

data class CurrentDto(
    val temp: Double,
    val feels_like: Double,
    val pressure: Double,
    val humidity: Int,
    val wind_speed: Double,
    val weather:List<IconDto>
)