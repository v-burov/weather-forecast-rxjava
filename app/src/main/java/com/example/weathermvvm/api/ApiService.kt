package com.example.weathermvvm.api

import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API Service
 */
interface ApiService {

    @GET("data/2.5/find")
    fun getCityListByName(
            @Query("q") cityName: String,
            @Query("APPID") apiKey: String
    ): Flowable<Response<WeatherResponse>>

    @GET(" data/2.5/weather")
    fun getCityByName (
        @Query("q") cityName: String,
        @Query("APPID") apiKey: String,
        @Query("units") metric: String = "metric"
    ): Flowable<Response<WeatherItem>>
}