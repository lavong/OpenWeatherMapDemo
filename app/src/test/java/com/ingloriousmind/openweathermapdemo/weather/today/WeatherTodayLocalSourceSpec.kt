package com.ingloriousmind.openweathermapdemo.weather.today

import com.ingloriousmind.openweathermapdemo.domain.model.Weather
import com.ingloriousmind.openweathermapdemo.domain.model.newMock
import com.ingloriousmind.openweathermapdemo.weather.WeatherDao
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class WeatherTodayLocalSourceSpec : Spek({

    val dao = mockk<WeatherDao>()
    val localSource by memoized { WeatherTodayLocalSource(dao) }

    val weather = Weather.newMock()
    val error = IllegalStateException("nah, not today.")

    describe("load") {

        context("success") {
            every { dao.getWeather() } returns Maybe.just(weather)

            val subscriber = localSource.load().test()

            it("should delegate call to dao") {
                verify { dao.getWeather() }
            }
            it("should not error") {
                subscriber
                    .assertValue(weather)
                    .assertNoErrors()
            }
        }

        context("error") {
            every { dao.getWeather() } returns Maybe.error(error)

            val subscriber = localSource.load().test()

            it("should delegate call to dao") {
                verify { dao.getWeather() }
            }
            it("should propagate error") {
                subscriber
                    .assertNoValues()
                    .assertError(error)
            }
        }
    }

    describe("store") {

        context("success") {
            every { dao.insert(weather) } returns Completable.complete()

            val subscriber = localSource.store(weather).test()

            it("should delegate to dao") {
                verify { dao.insert(weather) }
            }
            it("should return value") {
                subscriber
                    .assertNoErrors()
                    .assertValue(weather)
            }
        }

        context("error") {
            every { dao.insert(weather) } returns Completable.error(error)

            val subscriber = localSource.store(weather).test()

            it("should delegate to dao") {
                verify { dao.insert(weather) }
            }
            it("should propagate error") {
                subscriber
                    .assertNoValues()
                    .assertError(error)
            }
        }

    }

})
