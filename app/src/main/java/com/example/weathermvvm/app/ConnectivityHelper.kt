package com.example.weathermvvm.app

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper for [ConnectivityManager]
 */
@Singleton
class ConnectivityHelper @Inject constructor(context: Context) {

  private val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  val isConnected: Boolean get() = connectivityManager.activeNetworkInfo?.isConnected ?: false

}
