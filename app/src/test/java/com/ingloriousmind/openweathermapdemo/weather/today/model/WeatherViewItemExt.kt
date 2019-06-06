package com.ingloriousmind.openweathermapdemo.weather.today.model

fun WeatherViewItem.Companion.newMock() = WeatherViewItem(
    locationName = "some place sunny",
    locationCoordinates = "lat, lon",
    temperature = "30°C",
    iconUrl = "https://images.cdn/img.png",
    temperatureRange = "low-hi"
)
