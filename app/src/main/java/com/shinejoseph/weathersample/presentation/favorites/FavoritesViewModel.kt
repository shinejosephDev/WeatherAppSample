package com.shinejoseph.weathersample.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinejoseph.weathersample.data.model.City
import com.shinejoseph.weathersample.domain.usecase.GetCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getCityUseCase: GetCityUseCase
) : ViewModel() {

    init {
        getCities()
    }

    private val _state = MutableStateFlow(emptyList<City>())
    val state = _state.asStateFlow()

    fun getCities() {
        viewModelScope.launch {
            getCityUseCase().collect { list ->
                _state.emit(list)
            }
        }
    }


}