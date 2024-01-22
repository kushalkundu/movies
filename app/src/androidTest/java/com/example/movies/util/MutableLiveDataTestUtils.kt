package com.example.movies.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> MutableLiveData<T>.getOrAwaitValue(): T {
    var data: T? = null

    val latch = CountDownLatch(1)

    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            this@getOrAwaitValue.removeObserver(this)
            latch.countDown()
        }
    }

    this.observeForever(observer)

    try {
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw TimeoutException("Live Data Never gets the data")
        }
    } finally {
        this.removeObserver(observer)
    }




    return data as T
}