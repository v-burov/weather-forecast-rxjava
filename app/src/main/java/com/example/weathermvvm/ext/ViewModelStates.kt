package com.example.weathermvvm.ext

import androidx.lifecycle.MutableLiveData
import android.database.Cursor
import com.example.weathermvvm.ui.ListItem

/**
 * States for ViewModel
 */
sealed class ViewModelState

object StateIdle : ViewModelState()
data class StateError(val messageId: Int) : ViewModelState()
object StateWeatherDetails : ViewModelState()
object StateWeatherSearch : ViewModelState()
data class StateErrorException(val throwable: Throwable) : ViewModelState()
data class StateSetCityList(val cursor: Cursor) : ViewModelState()
data class StateSetCityOnMap(val lat: Double, val lon: Double) : ViewModelState()
data class StateSetWeatherDetails(val list: List<ListItem>) : ViewModelState()

class StateLiveData(state: ViewModelState = StateIdle) : MutableLiveData<ViewModelState>() {
  init {
    value = state
  }
}