package com.mparticle.example.higgsshopsampleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Get value from LiveData using suspend coroutine
 */
suspend fun <T> LiveData<T?>.getValueFromLiveData() = suspendCoroutine<T?> {
    var observer: Observer<T?>? = null
    observer = Observer<T?> { t ->
        observer?.let { this.removeObserver(it) }
        it.resume(t)
    }
    this.observeForever(observer)
}