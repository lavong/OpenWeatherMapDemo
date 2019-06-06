package com.ingloriousmind.openweathermapdemo.domain.model

fun Weather.Companion.newMock() = Weather(
    name = "some-weather",
    id = 1,
    timezone = 2,
    base = "sure.",
    temp = 23.42,
    pressure = 3,
    humidity = 4,
    tempMin = -273.0,
    tempMax = 47.9,
    lat = 1.0,
    lon = 1.0,
    iconId = "some-icon-id",
    lastUpdated = 1559822563
)
