package com.ingloriousmind.openweathermapdemo.weather.today

import com.ingloriousmind.openweathermapdemo.R
import com.ingloriousmind.openweathermapdemo.domain.model.TestSchedulerProvider
import com.ingloriousmind.openweathermapdemo.domain.model.Weather
import com.ingloriousmind.openweathermapdemo.domain.model.newMock
import com.ingloriousmind.openweathermapdemo.weather.today.model.WeatherViewItem
import com.ingloriousmind.openweathermapdemo.weather.today.model.mapper.WeatherViewItemMapper
import com.ingloriousmind.openweathermapdemo.weather.today.model.newMock
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.spekframework.spek2.Spek
import org.spekframework.spek2.lifecycle.CachingMode.SCOPE
import org.spekframework.spek2.style.specification.describe

class WeatherTodayViewModelSpec : Spek({

    val schedulerProvider = TestSchedulerProvider()
    val repository by memoized(SCOPE) { mockk<WeatherTodayRepositoryType>() }
    val mapper by memoized(SCOPE) { mockk<WeatherViewItemMapper>() }
    val viewModel by memoized(SCOPE) { WeatherTodayViewModel(schedulerProvider, repository, mapper) }

    val weather = Weather.newMock()
    val viewItem = WeatherViewItem.newMock()

    every { mapper.map(any<Weather>()) } returns viewItem

    describe("search") {

        context("success") {
            val location = "mainz"
            every { repository.weather(any()) } returns Single.just(weather)
            val weatherSubscriber = viewModel.outputs.weather().test()

            viewModel.search(location)

            it("should delegate search for given location to repository") {
                verify { repository.weather(location) }
            }
            it("should map business model to view item") {
                verify { mapper.map(weather) }
            }
            it("should emit weather view item") {
                weatherSubscriber
                    .assertNoErrors()
                    .assertValueCount(2)
                    .assertValueAt(1, viewItem)
            }
        }

        context("error") {
            val notInException = ArrayIndexOutOfBoundsException("brexit innit.")
            every { repository.weather(any()) } returns Single.error(notInException)
            val errorSubscriber = viewModel.outputs.error().test()

            viewModel.search("uk")

            it("should emit error") {
                errorSubscriber
                    .assertValue(R.string.error_generic)
            }
        }
    }

    describe("weather") {
        every { repository.weather(any()) } returns Single.just(weather)

        val subscriber = viewModel.outputs.weather().test()

        it("should trigger initial search with default location") {
            verify { repository.weather(WeatherTodayViewModel.DEFAULT_LOCATION) }
        }
        it("should provide weather") {
            subscriber
                .assertNoErrors()
                .assertValue(viewItem)
        }
    }

    describe("onSearchQuery") {

        val query = "some-location-to-look-up-weather-for"
        every { repository.weather(any()) } returns Single.just(weather)

        viewModel.inputs.onSearchQuery(query)

        it("should delegate search for given location to repository") {
            verify(exactly = 1) { repository.weather(query) }
        }
    }

})
