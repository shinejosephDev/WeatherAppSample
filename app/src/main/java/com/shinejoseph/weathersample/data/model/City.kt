package com.shinejoseph.weathersample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var lat: Float,
    var lng: Float,
    val name: String?,
)
