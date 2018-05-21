package com.example.weathermvvm.app

import com.example.weathermvvm.api.ApiModule
import com.example.weathermvvm.db.DbModule
import com.example.weathermvvm.job.WeatherJobService
import com.example.weathermvvm.ui.WeatherViewModel
import dagger.Component
import javax.inject.Singleton


/**
 * Provide Application scope dependencies
 */

@Singleton
@Component(modules = [(ApiModule::class), (AppModule::class), (DbModule::class)])
interface AppComponent {

    fun inject(weatherViewModel: WeatherViewModel)

    fun inject(weatherJobService: WeatherJobService)

}
