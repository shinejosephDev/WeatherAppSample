package com.shinejoseph.weathersample.data.local

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPrefs @Inject constructor(
    @ApplicationContext private val context: Context,
) : PrefsHelper {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    override fun saveUnit(units: String) {
        sharedPreferences.edit {
            putString(UNIT, units)
        }
    }

    override fun getUnits(): String {
        return sharedPreferences.getString(UNIT, null) ?: DEFAULT_UNIT
    }

    fun getText(): String {
        if (getUnits().equals(DEFAULT_UNIT))
            return "°C"
        else
            return "°F"
    }

    companion object {
        private const val UNIT = "pref_unit"
        private const val DEFAULT_UNIT = "metric"
    }
}
