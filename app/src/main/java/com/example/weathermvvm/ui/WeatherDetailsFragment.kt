package com.example.weathermvvm.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.weathermvvm.R
import com.example.weathermvvm.ext.*
import kotlinx.android.synthetic.main.fragment_weather_details.*
import android.view.*


/**
 * Weather details
 */

class WeatherDetailsFragment : Fragment() {

    private var viewModel: WeatherViewModel? = null
    private val adapter = WeatherDetailsAdapter()

    companion object {
        const val WEATHER_DETAILS_FRAGMENT_TAG = "WEATHER_DETAILS_FRAGMENT_TAG"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_weather_details, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val act = activity as AppCompatActivity
        viewModel = ViewModelProviders.of(act).get(WeatherViewModel::class.java)
                .also { act.appComponent.inject(it) }
                .also { it.state.observe(this, Observer { onStateChanged(it) }) }

        list.initList(adapter)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        viewModel?.initWeatherDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
            inflater?.inflate(R.menu.menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
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