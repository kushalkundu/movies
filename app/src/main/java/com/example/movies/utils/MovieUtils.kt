package com.example.movies.utils

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat

// Movie App Utility Class
object MovieUtils {

    fun checkNetwork(context: Context?): Boolean {
        return if (context != null) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) true else activeNetwork.type == ConnectivityManager.TYPE_MOBILE
            } else false

        } else {
            false
        }
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