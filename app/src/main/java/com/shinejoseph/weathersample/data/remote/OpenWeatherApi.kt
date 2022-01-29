package com.shinejoseph.weathersample.data.remote

import com.shinejoseph.weathersample.data.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("/data/2.5/onecall?")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("units") units: String,
        @Query("appid") appid: String,
    ): WeatherDto

    @GET("/data/2.5/onecall?")
    suspend fun getCityWeather(
        @Query("city") lat: String,
        @Query("units") units: String,
        @Query("appid") appid: String,
    ): WeatherDto
}