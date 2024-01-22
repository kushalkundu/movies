package com.example.movies

import android.app.Application
import androidx.core.os.BuildCompat
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp(Application::class)
class MoviesApplication : Hilt_MoviesApplication(){

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

}