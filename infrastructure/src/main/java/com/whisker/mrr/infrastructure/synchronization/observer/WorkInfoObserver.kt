package com.whisker.mrr.infrastructure.synchronization.observer

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import com.whisker.mrr.infrastructure.synchronization.exception.WorkCancelledException
import com.whisker.mrr.infrastructure.synchronization.exception.WorkFailedException
import io.reactivex.CompletableObserver

class WorkInfoObserver(
    private val observer: CompletableObserver,
    liveData: LiveData<List<WorkInfo>>
) : LiveDataObserver<List<WorkInfo>>(liveData) {

    override fun onChangedAndNotDisposed(value: List<WorkInfo>?) {
        value?.let { works ->
            if(works.isNotEmpty()) {
                when {
                    works.all { it.state == WorkInfo.State.SUCCEEDED } -> {
                        observer.onComplete()
                        dispose()
                    }
                    works.any { it.state == WorkInfo.State.FAILED } -> {
                        observer.onError(WorkFailedException(works.first { it.state == WorkInfo.State.FAILED }.id))
                        dispose()
                    }
                    works.any { it.state == WorkInfo.State.CANCELLED } -> {
                        observer.onError(WorkCancelledException(works.first { it.state == WorkInfo.State.CANCELLED }.id))
                        dispose()
                    }
                }
            }
        }
    }
}