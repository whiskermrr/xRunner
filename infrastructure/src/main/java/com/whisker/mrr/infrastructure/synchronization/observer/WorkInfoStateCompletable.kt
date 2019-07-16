package com.whisker.mrr.infrastructure.synchronization.observer

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import io.reactivex.Completable
import io.reactivex.CompletableObserver

class WorkInfoStateCompletable(private val liveData: LiveData<List<WorkInfo>>) : Completable() {

    override fun subscribeActual(observer: CompletableObserver) {
        val workInfoObserver = WorkInfoObserver(observer, liveData)
        liveData.observeForever(workInfoObserver)
        observer.onSubscribe(workInfoObserver)
    }
}