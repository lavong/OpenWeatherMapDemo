package com.ingloriousmind.openweathermapdemo.weather.today

import com.ingloriousmind.openweathermapdemo.domain.model.Weather
import com.ingloriousmind.openweathermapdemo.weather.WeatherDao
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber

interface WeatherTodayLocalSourceType {
    fun load(): Maybe<Weather>
    fun store(weather: Weather): Single<Weather>
}

class WeatherTodayLocalSource(
    private val dao: WeatherDao
) : WeatherTodayLocalSourceType {

    override fun load(): Maybe<Weather> {
        return dao.getWeather()
            .doOnSuccess { Timber.d("loaded from cache: $it") }
    }

    override fun store(weather: Weather): Single<Weather> {
        return dao.insert(weather)
            .andThen(Single.just(weather))
    }

}
