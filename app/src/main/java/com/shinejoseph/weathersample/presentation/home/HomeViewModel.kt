package com.shinejoseph.weathersample.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinejoseph.weathersample.common.Resource
import com.shinejoseph.weathersample.data.local.CurrentWeatherDao
import com.shinejoseph.weathersample.data.model.CurrentWeather
import com.shinejoseph.weathersample.domain.model.toWeatherEntity
import com.shinejoseph.weathersample.domain.usecase.GetCurrentWeatherUseCase
import com.shinejoseph.weathersample.domain.usecase.GetUnitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getUnitUseCase: GetUnitUseCase,
    private val dao: CurrentWeatherDao,
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state = _state.asStateFlow()

    fun getWeather(lat: Double, lng: Double) {
        getCurrentWeatherUseCase(lat, lng, getUnitUseCase.invoke()).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    dao.delete()
                    val list = mutableListOf<CurrentWeather>()
                    result.data?.forEach {
                        list.add(it.toWeatherEntity())
                    }
                    dao.insert(list)
                    _state.emit(WeatherState(weather = result.data!!))
                }

                is Resource.Error -> {
                    _state.emit(
                        WeatherState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    )
                }
                is Resource.Loading -> {
                    _state.emit(WeatherState(isLoading = true))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun insert() {
        viewModelScope.launch {

        }
    }
}