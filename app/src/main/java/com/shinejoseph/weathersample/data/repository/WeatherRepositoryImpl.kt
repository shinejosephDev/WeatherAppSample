package com.shinejoseph.weathersample.data.repository

import com.shinejoseph.weathersample.BuildConfig
import com.shinejoseph.weathersample.data.remote.OpenWeatherApi
import com.shinejoseph.weathersample.data.remote.dto.WeatherDto
import com.shinejoseph.weathersample.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenWeatherApi
) : WeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lng: Double, units: String): WeatherDto {
        return api.getCurrentWeather(lat, lng, units, BuildConfig.OPEN_WEATHER_MAP_APP_ID)
    }
}