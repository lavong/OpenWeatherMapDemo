package com.ingloriousmind.openweathermapdemo.weather.today

import com.ingloriousmind.openweathermapdemo.domain.model.WeatherResponseDto
import com.ingloriousmind.openweathermapdemo.domain.model.newMock
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import retrofit2.Retrofit

class WeatherTodayRemoteSourceSpec : Spek({

    val service = mockk<WeatherTodayRemoteSourceType>()
    val retrofit = mockk<Retrofit>()
    every { retrofit.create(WeatherTodayRemoteSourceType::class.java) } returns service
    val remoteSource by memoized { WeatherTodayRemoteSource(retrofit) }

    val dto = WeatherResponseDto.newMock()
    val error = IllegalStateException("u wish.")

    describe("load") {
        val location = "some-location"
        val appId = "some-app-id"
        val units = "whatever"

        context("success") {
            every { service.getWeather(any(), any(), any()) } returns Single.just(dto)

            val subscriber = remoteSource.getWeather(location, appId, units).test()

            it("should delegate call to service") {
                verify { service.getWeather(location, appId, units) }
            }
            it("should provide dto") {
                subscriber
                    .assertNoErrors()
                    .assertValue(dto)
            }
        }

        context("error") {
            every { service.getWeather(any(), any(), any()) } returns Single.error(error)

            val subscriber = remoteSource.getWeather(location, appId, units).test()

            it("should delegate call to service") {
                verify { service.getWeather(location, appId, units) }
            }
            it("should propagate error") {
                subscriber
                    .assertNoValues()
                    .assertError(error)
            }
        }

    }

})
