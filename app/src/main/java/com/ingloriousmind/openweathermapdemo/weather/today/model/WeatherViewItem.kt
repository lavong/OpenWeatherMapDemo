package com.ingloriousmind.openweathermapdemo.weather.today.model

data class WeatherViewItem(
    val locationName: String,
    val locationCoordinates: String,
    val temperature: String,
    val temperatureRange: String,
    val iconUrl: String?
) {
    companion object
}
