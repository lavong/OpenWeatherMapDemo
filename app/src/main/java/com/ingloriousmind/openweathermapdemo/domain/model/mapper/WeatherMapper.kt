package com.ingloriousmind.openweathermapdemo.domain.model.mapper

import com.ingloriousmind.openweathermapdemo.domain.Mapper
import com.ingloriousmind.openweathermapdemo.domain.model.Weather
import com.ingloriousmind.openweathermapdemo.domain.model.WeatherResponseDto

class WeatherMapper : Mapper<WeatherResponseDto, Weather>() {

    override fun map(from: WeatherResponseDto) = with(from) {
        Weather(
            name = name ?: throw IllegalArgumentException("name expected"),
            id = id ?: throw IllegalArgumentException("id expected"),
            timezone = timezone ?: 0,
            base = base ?: "",
            temp = main?.temp ?: throw IllegalArgumentException("temperature expected"),
            tempMin = main.temp_min ?: throw IllegalArgumentException("min temperature expected"),
            tempMax = main.temp_max ?: throw IllegalArgumentException("max temperature expected"),
            pressure = main.pressure ?: 0,
            humidity = main.humidity ?: 0,
            lat = coord?.lat ?: .0,
            lon = coord?.lon ?: .0,
            iconId = weather?.firstOrNull()?.icon
        )
    }

}
