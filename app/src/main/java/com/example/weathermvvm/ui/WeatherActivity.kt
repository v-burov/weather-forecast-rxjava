package com.example.weathermvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.ViewModelProvider
import com.example.weathermvvm.R
import com.example.weathermvvm.ext.*
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity() {

  private lateinit var viewModel: WeatherViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_weather)
    viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
      .also { appComponent.inject(it) }
      .also {
        it.progress.observe(this,
                            { isProgressShown -> if (isProgressShown) progress.show() else progress.gone() })
      }
      .also { it.state.observe(this, { state -> onStateChanged(state) }) }
      .also { if (savedInstanceState == null) it.loadFragment() }
  }

  private fun onStateChanged(state: ViewModelState?) = when (state) {
    is StateError -> showError(state.messageId)
    is StateErrorException -> showAlertDialog(state.throwable)
    is StateWeatherDetails -> replaceFragment(WeatherDetailsFragment(),
                                              WeatherDetailsFragment.WEATHER_DETAILS_FRAGMENT_TAG,
                                              intArrayOf(R.animator.slide_in_left,
                                                         R.animator.slide_in_right))
    is StateWeatherSearch -> replaceFragment(WeatherFragment(),
                                             WeatherFragment.WEATHER_FRAGMENT_TAG)
    else -> logClass(state.toString())
  }

  private fun showError(messageId: Int) {
    Snackbar.make(coordinatorLayout, messageId, Snackbar.LENGTH_INDEFINITE)
      .setDuration(LENGTH_SHORT)
      .show()
  }
}
