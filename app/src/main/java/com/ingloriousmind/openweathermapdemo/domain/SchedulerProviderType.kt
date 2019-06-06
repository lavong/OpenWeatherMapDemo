package com.ingloriousmind.openweathermapdemo.domain

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProviderType {

    fun io(): Scheduler
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun newThread(): Scheduler

}

class SchedulerProvider : SchedulerProviderType {

    override fun io() = Schedulers.io()

    override fun ui() = AndroidSchedulers.mainThread()

    override fun computation() = Schedulers.computation()

    override fun newThread() = Schedulers.newThread()

}
