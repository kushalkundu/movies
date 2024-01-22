package com.example.movies.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat

// Movie App Utility Class
object MovieUtils {

    fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    fun getFormattedDate(date: String?, inputFormat: String?, outputFormat: String?): String? {
        return if (date.isNullOrEmpty() || date.equals("null", ignoreCase = true)) {
            "-"
        } else try {
            val inputSdf = SimpleDateFormat(inputFormat)
            val mDate = inputSdf.parse(date)
            val outputSdf = SimpleDateFormat(outputFormat)
            if (mDate != null) {
                outputSdf.format(mDate)
            } else {
                "-"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }
}