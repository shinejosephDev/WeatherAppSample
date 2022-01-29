package com.shinejoseph.weathersample.domain.usecase

import com.shinejoseph.weathersample.common.Resource
import com.shinejoseph.weathersample.data.remote.dto.toWeather
import com.shinejoseph.weathersample.domain.model.Weather
import com.shinejoseph.weathersample.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    operator fun invoke(lat: Double, lng: Double, units: String): Flow<Resource<List<Weather>>> =
        flow {
            try {
                emit(Resource.Loading())
                val weather = repository.getCurrentWeather(lat, lng, units).toWeather()
                emit(Resource.Success(weather))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            }
        }
}