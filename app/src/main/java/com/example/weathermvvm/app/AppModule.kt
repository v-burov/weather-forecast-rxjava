package com.example.weathermvvm.app

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provide local (context) dependencies for [AppComponent]
 */
@Module
class AppModule(private val context: Context) {

  @Singleton
  @Provides
  fun provideContext(): Context = context

}

