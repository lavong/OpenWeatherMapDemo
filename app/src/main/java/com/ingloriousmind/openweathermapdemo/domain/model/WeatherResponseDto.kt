package com.ingloriousmind.openweathermapdemo.domain.model

data class WeatherResponseDto(
    val name: String?,
    val id: Int?,
    val timezone: Int?,
    val base: String?,
    val coord: WeatherCoordDto?,
    val weather: List<WeatherDto>?,
    val main: WeatherMainDto?
) {
    companion object
}

data class WeatherDto(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class WeatherMainDto(
    val temp: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val temp_min: Double?,
    val temp_max: Double?
)

data class WeatherCoordDto(
    val lat: Double?,
    val lon: Double?
)
