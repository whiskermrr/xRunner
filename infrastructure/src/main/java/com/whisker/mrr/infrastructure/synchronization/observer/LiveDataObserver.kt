package com.whisker.mrr.infrastructure.synchronization.observer

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.android.MainThreadDisposable

abstract class LiveDataObserver<T>(
    private val liveData: LiveData<T>
) : MainThreadDisposable(), Observer<T> {

    override fun onDispose() {
        stopObservingLiveData()
    }

    override fun onChanged(value: T?) {
        if(!isDisposed) {
            onChangedAndNotDisposed(value)
        }
    }

    abstract fun onChangedAndNotDisposed(value: T?)

    private fun stopObservingLiveData() {
        liveData.removeObserver(this)
    }
}