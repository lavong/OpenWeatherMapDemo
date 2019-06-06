package com.ingloriousmind.openweathermapdemo.weather.today.model.mapper

import com.ingloriousmind.openweathermapdemo.domain.Mapper
import com.ingloriousmind.openweathermapdemo.domain.model.Weather
import com.ingloriousmind.openweathermapdemo.weather.today.model.WeatherViewItem
import java.util.*

class WeatherViewItemMapper : Mapper<Weather, WeatherViewItem>() {

    override fun map(from: Weather): WeatherViewItem = with(from) {
        // TODO fix unit, extract strings
        WeatherViewItem(
            locationName = name,
            locationCoordinates = String.format(Locale.getDefault(), "(%.4f, %.4f)", lat, lon),
            temperature = String.format(Locale.getDefault(), "%.0f°C", temp),
            temperatureRange = String.format(Locale.getDefault(), "%.1f - %.1f°C", tempMin, tempMax),
            iconUrl = iconId?.let { String.format("https://openweathermap.org/img/w/%s.png", it) }
        )
    }

}
