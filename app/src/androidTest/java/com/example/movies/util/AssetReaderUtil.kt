
package com.example.movies.util

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

open class AssetReaderUtil {
    companion object {
        private val BUFFER_SIZE = 20 * 1024
        fun asset(context: Context, assetPath: String): String {
            try {
                val inputStream =
                    context.classLoader.getResourceAsStream("response/$assetPath")
                return inputStreamToString(inputStream, "UTF-8")
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }

        @Throws(IOException::class)
        fun inputStreamToString(inputStream: InputStream, charsetName: String): String {
            val builder = StringBuffer()
            val reader = InputStreamReader(inputStream, charsetName)
            val buffer = CharArray(BUFFER_SIZE)
            val length: Int = reader.read(buffer)
            builder.append(buffer, 0, length)
            return builder.toString()
        }
    }
}