package com.shinejoseph.weathersample.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shinejoseph.weathersample.data.model.City
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getCities() : Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City) : Long

    @Query("SELECT EXISTS(SELECT * FROM cities WHERE name = :name)")
    suspend fun isRowIsExist(name : String) : Boolean


    @Delete
    suspend fun delete(city: City)
}