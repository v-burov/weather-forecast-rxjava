package com.example.weathermvvm.ui

import android.app.SearchManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.text.TextUtils
import com.example.weathermvvm.R
import com.example.weathermvvm.api.WeatherItem
import com.example.weathermvvm.app.ConnectivityHelper
import com.example.weathermvvm.ext.*
import com.example.weathermvvm.repo.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.HashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * ViewModel holds buisness logic
 */

class WeatherViewModel : ViewModel() {

    @Inject lateinit var connectivityHelper: ConnectivityHelper
    @Inject lateinit var repository: WeatherRepository

    val progress = MutableLiveData<Boolean>()
    val state = StateLiveData()

    private val compositeDisposable = CompositeDisposable()
    private val isProcess: Boolean get() = progress.value == true
    private val mapCity = HashMap<String, WeatherItem>()

    fun loadFragment() {
        if (isProcess) return
        progress.value = true
        val s = repository.isWeatherCityAvailable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progress.value = false }
                .subscribe({ if (it) state.value = StateWeatherDetails else state.value = StateWeatherSearch },
                        { state.value = StateErrorException(it) })
        compositeDisposable.add(s)
    }

    fun findCity(cityName: String) {
        compositeDisposable.clear()
        val s = repository.getCityListByName(cityName)
                .doOnNext { mapCity.clear() }
                .doOnNext { it.forEach { mapCity["${it.name}, ${it.sys.country}"] = it } }
                .debounce(300, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state.value = StateSetCityList(buildSuggestionCursor(mapCity.keys.toList())) }, { logClass(it.message.toString()) })
        compositeDisposable.add(s)
    }

    fun setCityOnMap(cityName: String) {
        val city = mapCity[cityName]
        if (city == null) {
            state.value = StateError(R.string.city_not_found_error)
            return
        }
        state.value = StateSetCityOnMap(city.coord.lat, city.coord.lon)
    }

    fun openWeatherDetails(cityName: String) {
        if (isProcess) return
        if (TextUtils.isEmpty(cityName)) {
            state.value = StateError(R.string.error_empty_field)
            return
        }

        if (!connectivityHelper.isConnected) {
            state.value = StateError(R.string.error_not_connected)
            return
        }

        val city = mapCity[cityName]
        if (city == null) {
            state.value = StateError(R.string.city_not_found_error)
            return
        }

        progress.value = true
        val s = repository.getCityByName(cityName)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progress.value = false }
                .subscribe({ state.value = StateWeatherDetails }, { state.value = StateErrorException(it) })
        compositeDisposable.add(s)
    }

    fun initWeatherDetails() {
        progress.value = true
        val s = repository.getWeatherDetailsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progress.value = false }
                .subscribe({ state.value = StateSetWeatherDetails(it) }, { state.value = StateErrorException(it) })
        compositeDisposable.add(s)
    }

    fun changeCity() {
        progress.value = true
        val s = repository.clearData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progress.value = false }
                .subscribe({ state.value = StateWeatherSearch }, { logClass(it.message.toString()) })
        compositeDisposable.add(s)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    private fun buildSuggestionCursor(list: List<String>): Cursor {
        val columns = arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA)
        val cursor = MatrixCursor(columns)
        list.forEachIndexed { i, s ->
            val row = arrayOf(i.toString(), s, "COLUMN_DATA")
            cursor.addRow(row)
        }
        return cursor
    }
}