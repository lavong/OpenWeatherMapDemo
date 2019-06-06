package com.ingloriousmind.openweathermapdemo.weather.today

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.ingloriousmind.openweathermapdemo.R
import com.ingloriousmind.openweathermapdemo.view.BaseActivity
import com.ingloriousmind.openweathermapdemo.view.hideSoftKeyboard
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_weather_today.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherTodayActivity : BaseActivity() {

    private val weatherViewModel: WeatherTodayViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_today)

        subscriptions += weatherViewModel.outputs.weather()
            .subscribe {
                weather_location.text = it.locationName
                weather_coordinates.text = it.locationCoordinates
                weather_temp.text = it.temperature
                weather_temp_range.text = it.temperatureRange
                Glide.with(weather_icon.context).load(it.iconUrl).into(weather_icon)
            }

        subscriptions += weatherViewModel.outputs.error()
            .subscribe { Snackbar.make(weather_container, getString(it), Snackbar.LENGTH_SHORT).show() }

        subscriptions += weatherViewModel.outputs.loading()
            .subscribe { weather_progressbar.isVisible = it }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.weather_today, menu)

        (menu.findItem(R.id.action_search).actionView as SearchView)
            .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) = true
                    .also { hideSoftKeyboard() }
                    .also { weatherViewModel.inputs.onSearchQuery(query) }

                override fun onQueryTextChange(newText: String) = false
            })

        return super.onCreateOptionsMenu(menu)
    }

}
