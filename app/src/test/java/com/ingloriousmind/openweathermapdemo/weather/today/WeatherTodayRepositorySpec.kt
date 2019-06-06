package com.ingloriousmind.openweathermapdemo.weather.today

import com.ingloriousmind.openweathermapdemo.BuildConfig
import com.ingloriousmind.openweathermapdemo.domain.model.Weather
import com.ingloriousmind.openweathermapdemo.domain.model.WeatherResponseDto
import com.ingloriousmind.openweathermapdemo.domain.model.mapper.WeatherMapper
import com.ingloriousmind.openweathermapdemo.domain.model.newMock
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Maybe
import io.reactivex.Single
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class WeatherTodayRepositorySpec : Spek({

    val localSource = mockk<WeatherTodayLocalSourceType>()
    val remoteSource = mockk<WeatherTodayRemoteSourceType>()
    val mapper = mockk<WeatherMapper>()
    val repository by memoized { WeatherTodayRepository(localSource, remoteSource, mapper) }

    val error = IllegalStateException("¯\\_(ツ)_/¯")
    val localWeather = Weather.newMock().copy(
        name = "existing data from local storage",
        lastUpdated = System.currentTimeMillis() - 1
    )
    val remoteWeather = Weather.newMock().copy(name = "new data from remote")
    val remoteWeatherDto = WeatherResponseDto.newMock().copy(name = "fresh data fetched from network")
    every { mapper.map(any<WeatherResponseDto>()) } returns remoteWeather

    describe("weather") {
        val location = localWeather.name

        context("local data valid") {
            every { localSource.load() } returns Maybe.just(localWeather)
            every { remoteSource.getWeather(any(), any(), any()) } returns Single.error(error)

            val subscriber = repository.weather(location).test()

            it("should provide local data") {
                verify { localSource.load() }
                subscriber
                    .assertNoErrors()
                    .assertValue(localWeather)
            }
            it("should not provide remote data") {
                subscriber
                    .assertValue { it.name != remoteWeather.name }
            }
        }

        context("local data outdated") {
            val outdatedLocalWeather = localWeather.copy(
                lastUpdated = System.currentTimeMillis() - (2000 * WeatherTodayRepository.VALIDITY_THRESHOLD_SECONDS)
            )
            every { localSource.load() } returns Maybe.just(outdatedLocalWeather)
            every { remoteSource.getWeather(any(), any(), any()) } returns Single.just(remoteWeatherDto)
            every { localSource.store(remoteWeather) } returns Single.just(remoteWeather)

            val subscriber = repository.weather(location).test()

            it("should hit network and provide fresh data") {
                verify { remoteSource.getWeather(location, BuildConfig.OPEN_WEATHER_MAPS_APP_ID, any()) }
                subscriber
                    .assertNoErrors()
                    .assertValue(remoteWeather)
            }
            it("should cache remote data in local storage") {
                verify { localSource.store(remoteWeather) }
            }
            it("should map dto to business object") {
                verify { mapper.map(remoteWeatherDto) }
            }
        }

        context("no local data for location requested") {
            val locationWithNoCachedData = "some-fancy-location-not-requested-before"
            every { localSource.load() } returns Maybe.just(localWeather)
            every { remoteSource.getWeather(any(), any(), any()) } returns Single.just(remoteWeatherDto)
            every { localSource.store(remoteWeather) } returns Single.just(remoteWeather)

            val subscriber = repository.weather(locationWithNoCachedData).test()

            it("should hit network and provide fresh data") {
                verify { remoteSource.getWeather(locationWithNoCachedData, BuildConfig.OPEN_WEATHER_MAPS_APP_ID, any()) }
                subscriber
                    .assertNoErrors()
                    .assertValue(remoteWeather)
            }
            it("should cache remote data in local storage") {
                verify { localSource.store(remoteWeather) }
            }
        }

        context("error reading from local storage") {
            every { localSource.load() } returns Maybe.error(error)
            every { remoteSource.getWeather(any(), any(), any()) } returns Single.just(remoteWeatherDto)

            val subscriber = repository.weather(location).test()

            it("should propagate error") {
                subscriber
                    .assertError(error)
                    .assertNoValues()
            }
        }

        context("error fetching from network") {
            every { localSource.load() } returns Maybe.just(localWeather)
            every { remoteSource.getWeather(any(), any(), any()) } returns Single.error(error)

            val subscriber = repository.weather("abc").test()

            it("should propagate error") {
                subscriber
                    .assertError(error)
                    .assertNoValues()
            }
        }

        context("error writing to local storage") {
            every { localSource.load() } returns Maybe.just(localWeather)
            every { remoteSource.getWeather(any(), any(), any()) } returns Single.just(remoteWeatherDto)
            every { localSource.store(remoteWeather) } returns Single.error(error)

            val subscriber = repository.weather("def").test()

            it("should propagate error") {
                subscriber
                    .assertError(error)
                    .assertNoValues()
            }
        }

    }

    describe("weatherLatestLocal") {

        context("success") {
            every { localSource.load() } returns Maybe.just(localWeather)

            val subscriber = repository.weatherLatestLocal().test()

            it("should delegate call to local source") {
                verify { localSource.load() }
            }
            it("should provide data") {
                subscriber
                    .assertNoErrors()
                    .assertValue(localWeather)
            }
        }

        context("error") {
            every { localSource.load() } returns Maybe.error(error)

            val subscriber = repository.weatherLatestLocal().test()

            it("should propagate error") {
                subscriber
                    .assertError(error)
                    .assertNoValues()
            }
        }
    }


})
