package com.ingloriousmind.openweathermapdemo.weather.today

import com.ingloriousmind.openweathermapdemo.R
import com.ingloriousmind.openweathermapdemo.domain.SchedulerProviderType
import com.ingloriousmind.openweathermapdemo.viewmodel.StatefulViewModel
import com.ingloriousmind.openweathermapdemo.viewmodel.StatefulViewModelInputs
import com.ingloriousmind.openweathermapdemo.viewmodel.StatefulViewModelOutputs
import com.ingloriousmind.openweathermapdemo.weather.today.model.WeatherViewItem
import com.ingloriousmind.openweathermapdemo.weather.today.model.mapper.WeatherViewItemMapper
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

interface WeatherTodayViewModelInputs : StatefulViewModelInputs {
    fun onSearchQuery(query: String)
}

interface WeatherTodayViewModelOutputs : StatefulViewModelOutputs {
    fun weather(): Observable<WeatherViewItem>
}

class WeatherTodayViewModel(
    private val schedulerProvider: SchedulerProviderType,
    private val repository: WeatherTodayRepositoryType,
    private val viewItemMapper: WeatherViewItemMapper
) : StatefulViewModel(schedulerProvider), WeatherTodayViewModelInputs, WeatherTodayViewModelOutputs {

    val inputs: WeatherTodayViewModelInputs
        get() = this

    val outputs: WeatherTodayViewModelOutputs
        get() = this

    private val weather = PublishSubject.create<WeatherViewItem>()

    init {
        search(DEFAULT_LOCATION)
    }

    internal fun search(location: String) {
        subscriptions += repository.weather(location)
            .doOnSubscribe { loading.onNext(true) }
            .doFinally { loading.onNext(false) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .map { viewItemMapper.map(it) }
            .subscribe(
                { weather.onNext(it) },
                {
                    Timber.e(it, "failed fetching weather")
                    error.onNext(R.string.error_generic)
                }
            )
    }

    override fun weather(): Observable<WeatherViewItem> =
        weather.observeOn(schedulerProvider.ui()).hide()

    override fun onSearchQuery(query: String) {
        search(query)
    }

    companion object {
        const val DEFAULT_LOCATION = "Frankfurt"
    }

}
