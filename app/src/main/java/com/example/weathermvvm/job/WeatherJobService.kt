package com.example.weathermvvm.job

import android.app.job.JobParameters
import android.app.job.JobService
import com.example.weathermvvm.app.WeatherApplication
import com.example.weathermvvm.ext.logClass
import com.example.weathermvvm.repo.WeatherRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Schedule the start of updating weather every 24 hours
 */

class WeatherJobService : JobService() {

  @Inject lateinit var repository: WeatherRepository

  private val compositeDisposable = CompositeDisposable()

  override fun onCreate() {
    super.onCreate()
    val app = application as WeatherApplication
    app.appComponent.inject(this)
  }

  override fun onStartJob(p0: JobParameters?): Boolean {
    val s = repository.schedule().subscribeOn(Schedulers.io())
      .subscribe({ logClass("Scheduler sync success") },
                 { logClass(it.message ?: "Scheduler sync failure") })
    compositeDisposable.add(s)
    return true
  }

  override fun onStopJob(p0: JobParameters?): Boolean {
    compositeDisposable.clear()
    logClass("Job scheduler stops")
    return true
  }
}