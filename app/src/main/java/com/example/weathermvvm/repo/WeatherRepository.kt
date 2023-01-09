package com.example.weathermvvm.repo

import android.content.Context
import com.example.weathermvvm.R
import com.example.weathermvvm.api.ApiService
import com.example.weathermvvm.api.WeatherItem
import com.example.weathermvvm.db.DaoSession
import com.example.weathermvvm.db.Weather
import com.example.weathermvvm.ext.getWeatherImagePath
import com.example.weathermvvm.ui.ImageListItem
import com.example.weathermvvm.ui.ListItem
import com.example.weathermvvm.ui.ListItemType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  Provide data from [ApiService]
 *  Repository gets data from api and sav eit to db
 */

@Singleton
class WeatherRepository @Inject constructor() {

  @Inject lateinit var apiService: ApiService
  @Inject lateinit var daoSession: DaoSession
  @Inject lateinit var context: Context
  private val apiKey = "12c9debee0da8daea1654d42aba95fd9"

  fun getCityListByName(cityName: String): Flowable<List<WeatherItem>> =
    apiService.getCityListByName(cityName, apiKey)
      .map { if (it.isSuccessful) it.body() else null }
      .map { weatherResponse -> weatherResponse.list.filter { it.sys.country != null } }

  fun getCityByName(cityName: String): Flowable<Unit> =
    apiService.getCityByName(cityName, apiKey)
      .map { if (it.isSuccessful) it.body() else throw RuntimeException("City not found") }
      .map { saveCityToDb(it) }

  fun getWeatherDetailsFromDb(): Single<List<ListItem>> =
    Single.just(daoSession.weatherDao)
      .map { it.queryBuilder().list().firstOrNull() }
      .map {
        val items = ArrayList<ListItem>()
        items.apply {
          add(ImageListItem(ListItemType.IMAGE_ITEM,
                            "${it.name}, ${it.country}",
                            context.getWeatherImagePath(it.icon)))
          add(ListItem(ListItemType.ITEM,
                       context.getString(R.string.temperature),
                       it.temperature.toString()))
          add(ListItem(ListItemType.ITEM,
                       context.getString(R.string.pressure),
                       it.pressure.toString()))
          add(ListItem(ListItemType.ITEM,
                       context.getString(R.string.humidity),
                       it.humidity.toString()))
          add(ListItem(ListItemType.ITEM,
                       context.getString(R.string.wind_speed),
                       it.windSpeed.toString()))
        }
      }

  fun clearData(): Completable =
    Completable.fromSingle(Single.just(daoSession.weatherDao).map { it.deleteAll() })

  fun isWeatherCityAvailable(): Single<Boolean> = Single.just(
    daoSession.weatherDao).map { it.queryBuilder().list().isNotEmpty() }

  fun schedule(): Flowable<Unit> {
    val item = daoSession.weatherDao.queryBuilder().list().firstOrNull()
    return getCityByName("${item?.name}, ${item?.country}")
  }

  private fun saveCityToDb(weatherDetails: WeatherItem?) {
    if (weatherDetails != null) {
      var weather = daoSession.weatherDao.queryBuilder().list().firstOrNull()
      var isNewWeatherCreated = false
      if (weather == null) {
        weather = Weather()
        isNewWeatherCreated = true
      }

      weather.apply {
        name = weatherDetails.name
        country = weatherDetails.sys.country
        temperature = weatherDetails.main.temp
        pressure = weatherDetails.main.pressure
        humidity = weatherDetails.main.humidity
        windSpeed = weatherDetails.wind.speed
        icon = weatherDetails.weather[0].icon
      }

      if (isNewWeatherCreated) {
        daoSession.weatherDao.insert(weather)
      } else {
        daoSession.weatherDao.update(weather)
      }
    }
  }
}