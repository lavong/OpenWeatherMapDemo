package com.ingloriousmind.openweathermapdemo.domain.model

fun WeatherResponseDto.Companion.newMock() = WeatherResponseDto(
    name = "some-name",
    id = 1,
    timezone = 2,
    base = "some-base",
    coord = WeatherCoordDto(1.0, 1.0),
    weather = listOf(WeatherDto(1, "some-main", "some-desc", "some-icon-id")),
    main = WeatherMainDto(11.0, 12, 13, 0.0, 11.0)
)
