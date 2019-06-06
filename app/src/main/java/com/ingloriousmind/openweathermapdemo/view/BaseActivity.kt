package com.ingloriousmind.openweathermapdemo.view

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val subscriptions = CompositeDisposable()

    override fun onDestroy() {
        subscriptions.clear()
        super.onDestroy()
    }

}
