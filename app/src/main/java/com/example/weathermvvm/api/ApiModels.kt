package com.example.weathermvvm.api

/**
 * Response models of [ApiService]
 */

data class WeatherResponse(
        val list: ArrayList<WeatherItem>
)

data class WeatherItem(
        val id: Int,
        val name: String,
        val coord: Coord,
        val main: Main,
        val wind: Wind,
        val sys: Sys,
        val weather: List<Weather>
)

data class Coord(
        val lon: Double,
        val lat: Double
)

data class Main(
        val temp: Double,
        val pressure: Double,
        val humidity: Int
)

data class Wind(val speed: Double)
data class Weather(val icon: String)
data class Sys(val country: String?)

