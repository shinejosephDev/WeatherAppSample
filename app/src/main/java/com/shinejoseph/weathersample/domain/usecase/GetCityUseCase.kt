package com.shinejoseph.weathersample.domain.usecase

import androidx.annotation.WorkerThread
import com.shinejoseph.weathersample.data.local.CityDao
import com.shinejoseph.weathersample.data.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCityUseCase @Inject constructor(
    private val dao: CityDao
) {
    @WorkerThread
    operator fun invoke(): Flow<List<City>>  {
       return dao.getCities()
    }
}