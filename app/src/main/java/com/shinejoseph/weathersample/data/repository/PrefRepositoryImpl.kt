package com.shinejoseph.weathersample.data.repository

import com.shinejoseph.weathersample.data.local.PrefsHelper
import com.shinejoseph.weathersample.domain.repository.PrefRepository
import javax.inject.Inject

class PrefRepositoryImpl @Inject constructor(
    private val prefsHelper: PrefsHelper
) : PrefRepository {

    override fun getUnit(): String {
        return prefsHelper.getUnits()
    }
}
