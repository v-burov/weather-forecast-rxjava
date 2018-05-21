package com.example.weathermvvm.ext

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.weathermvvm.R
import com.example.weathermvvm.app.WeatherApplication
import com.example.weathermvvm.app.AppComponent
import com.example.weathermvvm.ui.ErrorDialogFragment

/**
 * Activity extensions
 */
val AppCompatActivity.appComponent: AppComponent get() = (application as WeatherApplication).appComponent

fun AppCompatActivity.showAlertDialog(throwable: Throwable) {
    throwable.printStackTrace()
    throwable.message?.let { showErrorDialog(it) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, fragmentTag: String, anim: IntArray? = null) {
    val transaction = supportFragmentManager.beginTransaction()
    if (anim != null) {
        transaction.setCustomAnimations(anim[0], anim[1])
    }
    transaction.replace(R.id.frameContainer, fragment, fragmentTag)
    transaction.commit()
}

fun Context.getWeatherImagePath(image: String?) = "${getString(R.string.URL)}img/w/$image.png"

fun Any.logClass(message: String) = log(message = "${javaClass.simpleName}:$message")

fun View.show() = let { visibility = View.VISIBLE }

fun View.gone() = let { visibility = View.GONE }

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun RecyclerView.initList(adapter: RecyclerView.Adapter<*>) {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    layoutManager.isSmoothScrollbarEnabled = true
    this.layoutManager = layoutManager
    this.adapter = adapter
}

private fun log(message: String, tag: String = "TAG") {
    val prefix = "***"
    val prefixTag = if (!tag.startsWith(prefix)) "$prefix$tag" else tag
    Log.d(prefixTag, message)
}

private fun AppCompatActivity.showErrorDialog(message: String) {
    var fragment: ErrorDialogFragment? = fragmentManager.findFragmentByTag(ErrorDialogFragment.ERROR_FRAGMENT_DIALOG_TAG) as ErrorDialogFragment?
    if (fragment == null) {
        fragment = ErrorDialogFragment.newInstance(message)
    }
    fragment.show(fragmentManager, ErrorDialogFragment.ERROR_FRAGMENT_DIALOG_TAG)
}

