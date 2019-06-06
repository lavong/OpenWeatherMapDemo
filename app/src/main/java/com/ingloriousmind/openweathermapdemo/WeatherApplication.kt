package com.ingloriousmind.openweathermapdemo

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.ingloriousmind.openweathermapdemo.domain.SchedulerProvider
import com.ingloriousmind.openweathermapdemo.domain.SchedulerProviderType
import com.ingloriousmind.openweathermapdemo.domain.model.mapper.WeatherMapper
import com.ingloriousmind.openweathermapdemo.weather.WeatherDb
import com.ingloriousmind.openweathermapdemo.weather.today.*
import com.ingloriousmind.openweathermapdemo.weather.today.model.mapper.WeatherViewItemMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class WeatherApplication : Application() {

    private val appModule = module {
        single {
            Room.databaseBuilder(
                applicationContext,
                WeatherDb::class.java,
                WeatherDb.NAME
            ).build()
        }
        factory { get<WeatherDb>().weatherDao() }

        single<SchedulerProviderType> { SchedulerProvider() }

        single { Gson() }
        single { HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE } }
        single {
            OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .build()
        }
        single {
            Retrofit.Builder()
                .baseUrl(BuildConfig.OPEN_WEATHER_MAPS_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(get()))
                .client(get())
                .build()
        }

        single<WeatherTodayLocalSourceType> { WeatherTodayLocalSource(get()) }
        single<WeatherTodayRemoteSourceType> { WeatherTodayRemoteSource(get()) }
        single { WeatherMapper() }
        single { WeatherViewItemMapper() }
        single<WeatherTodayRepositoryType> { WeatherTodayRepository(get(), get(), get()) }
        viewModel { WeatherTodayViewModel(get(), get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@WeatherApplication)
            modules(appModule)
        }
    }

}
