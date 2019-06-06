package com.ingloriousmind.openweathermapdemo.domain.model

import com.ingloriousmind.openweathermapdemo.domain.SchedulerProviderType
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : SchedulerProviderType {
    override fun io() = Schedulers.trampoline()
    override fun ui() = Schedulers.trampoline()
    override fun computation() = Schedulers.trampoline()
    override fun newThread() = Schedulers.trampoline()
}
