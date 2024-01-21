package com.example.movies.utils

import android.content.Context
import android.content.SharedPreferences

// Movies App Shared Preferences
class PreferenceUtils(private val context: Context) {

    private fun getSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            Constants.SHARED_PREFS,
            Context.MODE_PRIVATE
        )
    }

    fun saveInt(key: String, value: Int) {
        getSharedPreference(context).edit().putInt(key, value).apply()
    }

    fun getPrefInt(key: String): Int {
        return getSharedPreference(context).getInt(key, 0)
    }
}