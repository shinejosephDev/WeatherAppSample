package com.shinejoseph.weathersample.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinejoseph.weathersample.common.Resource
import com.shinejoseph.weathersample.data.local.CityDao
import com.shinejoseph.weathersample.data.model.City
import com.shinejoseph.weathersample.domain.usecase.GetCurrentWeatherUseCase
import com.shinejoseph.weathersample.domain.usecase.GetUnitUseCase
import com.shinejoseph.weathersample.presentation.home.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val dao: CityDao,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getUnitUseCase: GetUnitUseCase,
) : ViewModel() {

     val isExist = MutableLiveData<Boolean>()

    private val _state = MutableStateFlow(WeatherState())
    val state = _state.asStateFlow()

    fun insertCity(name: String, lat: Float, lng: Float) {
        viewModelScope.launch {
            dao.insert(City(name = name,
                lat = lat,
                lng = lng
            ))
        }
    }

    fun isExist(name: String) {
        viewModelScope.launch {
            isExist.value = dao.isRowIsExist(name)
        }
    }

    fun getWeather(lat: Double, lng: Double) {
        getCurrentWeatherUseCase(lat, lng, getUnitUseCase.invoke()).onEach { result ->
            when (result) {
                is Resource.Success -> {
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


}