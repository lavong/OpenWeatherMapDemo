package com.ingloriousmind.openweathermapdemo.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val subscriptions = CompositeDisposable()

    override fun onCleared() {
        subscriptions.clear()
    }

}
