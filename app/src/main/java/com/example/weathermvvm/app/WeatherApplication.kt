package com.example.weathermvvm.app

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import com.example.weathermvvm.BuildConfig
import com.example.weathermvvm.ext.logClass
import com.example.weathermvvm.job.WeatherJobService
import com.facebook.stetho.Stetho

/**
 * Instance of Application, provide Singleton dependencies via [AppComponent]
 */

class WeatherApplication : Application() {

  val appComponent: AppComponent by lazy { buildAppComponent() }

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)
    scheduleJob()
  }

  private fun buildAppComponent(): AppComponent = DaggerAppComponent.builder()
    .appModule(AppModule(applicationContext))
    .build()
    .also { logClass("buildAppComponent finished") }

  private fun scheduleJob() {
    val jobId = 1001
    val interval = 86400000L
    val flexInterval = 82800000L
    val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
    val serviceName = ComponentName(this, WeatherJobService::class.java)
    val jobInfo = JobInfo.Builder(jobId, serviceName)
      .setPeriodic(interval, flexInterval)
      .build()
    jobScheduler.schedule(jobInfo)
  }

}
