package com.shinejoseph.weathersample.data.local

interface PrefsHelper {
    fun saveUnit(units: String)

    fun getUnits(): String
}