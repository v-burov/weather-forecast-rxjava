package com.example.weathermvvm.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import com.example.weathermvvm.R
import com.example.weathermvvm.ext.*
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
                .also { appComponent.inject(it) }
                .also { it.progress.observe(this, Observer { if (it == true) progress.show() else progress.gone() }) }
                .also { it.state.observe(this, Observer { onStateChanged(it) }) }
                .also { if (savedInstanceState == null) it.loadFragment()}
    }

    private fun onStateChanged(state: ViewModelState?) = when (state) {
            is StateError -> showError(state.messageId)
            is StateErrorException -> showAlertDialog(state.throwable)
            is StateWeatherDetails -> replaceFragment(WeatherDetailsFragment(), WeatherDetailsFragment.WEATHER_DETAILS_FRAGMENT_TAG, intArrayOf(R.animator.slide_in_left, R.animator.slide_in_right))
            is StateWeatherSearch ->  replaceFragment(WeatherFragment(), WeatherFragment.WEATHER_FRAGMENT_TAG)
            else -> logClass(state.toString())
    }

    private fun showError(messageId: Int) {
        Snackbar.make(coordinatorLayout, messageId, Snackbar.LENGTH_INDEFINITE)
                .setDuration(Toast.LENGTH_SHORT)
                .show()
    }
}
