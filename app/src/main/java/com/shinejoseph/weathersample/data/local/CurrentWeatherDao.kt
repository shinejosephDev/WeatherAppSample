package com.shinejoseph.weathersample.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shinejoseph.weathersample.data.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {
    @Query("SELECT * FROM current_weather")
     fun getWeatherList(): Flow<List<CurrentWeather>>

    @Query("DELETE FROM current_weather")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeather: List<CurrentWeather>)
}