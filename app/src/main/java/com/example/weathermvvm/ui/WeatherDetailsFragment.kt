package com.example.weathermvvm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.weathermvvm.R
import com.example.weathermvvm.ext.*
import kotlinx.android.synthetic.main.fragment_weather_details.*
import android.view.*
import androidx.lifecycle.ViewModelProvider

/**
 * Weather details
 */

class WeatherDetailsFragment : Fragment() {

  private var viewModel: WeatherViewModel? = null
  private val adapter = WeatherDetailsAdapter()

  companion object {
    const val WEATHER_DETAILS_FRAGMENT_TAG = "WEATHER_DETAILS_FRAGMENT_TAG"
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View = inflater.inflate(R.layout.fragment_weather_details, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val act = activity as AppCompatActivity
    viewModel = ViewModelProvider(act).get(WeatherViewModel::class.java)
      .also { act.appComponent.inject(it) }
      .also { it.state.observe(viewLifecycleOwner, { state -> onStateChanged(state) }) }

    list.initList(adapter)
    setHasOptionsMenu(true)
  }

  override fun onResume() {
    super.onResume()
    viewModel?.initWeatherDetails()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_fragment, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_change_city -> {
        viewModel?.changeCity()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun onStateChanged(state: ViewModelState?) = when (state) {
    is StateSetWeatherDetails -> adapter.setData(state.list)
    else -> logClass("$state")
  }
}