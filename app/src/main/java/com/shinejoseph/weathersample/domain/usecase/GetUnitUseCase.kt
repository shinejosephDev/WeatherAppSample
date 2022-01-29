package com.shinejoseph.weathersample.domain.usecase

import com.shinejoseph.weathersample.domain.repository.PrefRepository
import javax.inject.Inject

class GetUnitUseCase @Inject constructor(
    private val repository: PrefRepository
) {
    operator fun invoke(): String {
        return repository.getUnit()
    }
}