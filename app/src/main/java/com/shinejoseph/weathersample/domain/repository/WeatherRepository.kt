package com.shinejoseph.weathersample.domain.repository

import com.shinejoseph.weathersample.data.remote.dto.WeatherDto


interface WeatherRepository {

    suspend fun getCurrentWeather(lat: Double, lng: Double, units: String): WeatherDto
}