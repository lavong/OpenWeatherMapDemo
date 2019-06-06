package com.ingloriousmind.openweathermapdemo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @ColumnInfo val name: String,
    @PrimaryKey val id: Int,
    @ColumnInfo val timezone: Int,
    @ColumnInfo val base: String,
    @ColumnInfo val temp: Double,
    @ColumnInfo val pressure: Int,
    @ColumnInfo val humidity: Int,
    @ColumnInfo val tempMin: Double,
    @ColumnInfo val tempMax: Double,
    @ColumnInfo val lat: Double,
    @ColumnInfo val lon: Double,
    @ColumnInfo val iconId: String?,
    @ColumnInfo val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object
}
