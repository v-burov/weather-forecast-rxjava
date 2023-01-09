package com.example.weathermvvm.db

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provide DB (GreenDao) local dependencies
 */
@Suppress("ConstantConditionIf")
@Module
class DbModule {

  companion object {
    const val DB_NAME = "weather.db"
  }

  @Singleton
  @Provides
  internal fun provideDaoSession(context: Context): DaoSession {
    val helper = DaoMaster.DevOpenHelper(context, DB_NAME)
    val db = helper.writableDb
    return DaoMaster(db).newSession()
  }
}
