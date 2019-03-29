package com.example.weathermvvm.ui

import android.app.SearchManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.text.InputType
import android.text.TextUtils
import android.view.*
import android.widget.CursorAdapter
import android.widget.SimpleCursorAdapter
import com.example.weathermvvm.R
import com.example.weathermvvm.ext.*
import kotlinx.android.synthetic.main.fragment_weather.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Weather fragment lets to choose a city and display it on the map
 */

class WeatherFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: WeatherViewModel
    private var map: GoogleMap? = null

    private val searchSuggestionAdapter: CursorAdapter by lazy {
        SimpleCursorAdapter(
                activity,
                android.R.layout.simple_dropdown_item_1line,
                null,
                arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
                intArrayOf(android.R.id.text1),
                0
        )
    }

    companion object {
        const val WEATHER_FRAGMENT_TAG = "WEATHER_FRAGMENT_TAG"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_weather, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapView.let {
            it.onCreate(savedInstanceState)
            it.getMapAsync(this)
            it.onResume()
        }

        val act = activity as AppCompatActivity
        viewModel = ViewModelProviders.of(act).get(WeatherViewModel::class.java)
                .also { act.appComponent.inject(it) }
                .also { it.state.observe(this, Observer { onStateChanged(it) }) }
        buttonOk.setOnClickListener { viewModel.openWeatherDetails(searchView?.query.toString()) }

        searchView.apply {
            setIconifiedByDefault(false)
            queryHint = getString(R.string.enter_city_name)
            suggestionsAdapter = searchSuggestionAdapter
            inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

            setOnSuggestionListener(object : SearchView.OnSuggestionListener, android.widget.SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    searchSuggestionAdapter.cursor.moveToPosition(position)
                    val value = searchSuggestionAdapter.cursor.getString(1)
                    searchView?.setQuery(value, false)
                    viewModel.setCityOnMap(value)
                    return true
                }
            })

            setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(s: String): Boolean {
                    viewModel.findCity(s)
                    return true
                }
            })
            setOnCloseListener {
                if (!TextUtils.isEmpty(searchView?.query)) {
                    searchView?.setQuery(null, true)
                }
                true
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
    }

    private fun onStateChanged(state: ViewModelState?) = when (state) {
        is StateSetCityList -> updateCityList(state.cursor)
        is StateSetCityOnMap -> { searchView.hideKeyboard(); setCityOnMap(state.lat, state.lon) }
        else -> logClass("state $state")
    }

    private fun updateCityList(cursor: Cursor) {
        searchSuggestionAdapter.swapCursor(cursor)
    }

    private fun setCityOnMap(lat: Double, lon: Double) {
        val latLng = LatLng(lat, lon)
        val options = MarkerOptions().apply { position(latLng) }
        map?.apply {
            clear()
            moveCamera(CameraUpdateFactory.newLatLng(latLng))
            animateCamera(CameraUpdateFactory.zoomTo(7f), 1000, null)
            addMarker(options)
        }
    }

}