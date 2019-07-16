package com.whisker.mrr.infrastructure

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import com.whisker.mrr.infrastructure.synchronization.observer.WorkInfoStateCompletable
import io.reactivex.Completable

fun LiveData<List<WorkInfo>>.toWorkInfoStateSingle() : Completable {
    return WorkInfoStateCompletable(this)
}