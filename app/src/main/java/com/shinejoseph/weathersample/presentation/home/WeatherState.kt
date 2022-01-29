package com.shinejoseph.weathersample.presentation.home

import com.shinejoseph.weathersample.domain.model.Weather

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: List<Weather> = emptyList(),
    val error: String = ""
)
