package com.ingloriousmind.openweathermapdemo.viewmodel

import com.ingloriousmind.openweathermapdemo.domain.SchedulerProviderType
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface StatefulViewModelInputs

interface StatefulViewModelOutputs {
    fun loading(): Observable<Boolean>
    fun error(): Observable<Int>
}

abstract class StatefulViewModel(
    private val schedulerProvider: SchedulerProviderType
) : BaseViewModel(),
    StatefulViewModelInputs, StatefulViewModelOutputs {

    protected val loading = BehaviorSubject.createDefault(false)

    protected val error = PublishSubject.create<Int>()

    override fun loading(): Observable<Boolean> =
        loading.observeOn(schedulerProvider.ui()).hide()

    override fun error(): Observable<Int> =
        error.observeOn(schedulerProvider.ui()).hide()

}
