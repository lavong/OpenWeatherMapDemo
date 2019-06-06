package com.ingloriousmind.openweathermapdemo.weather

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ingloriousmind.openweathermapdemo.domain.model.Weather
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather ORDER BY lastUpdated DESC")
    fun getWeather(): Maybe<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg weather: Weather): Completable

}
