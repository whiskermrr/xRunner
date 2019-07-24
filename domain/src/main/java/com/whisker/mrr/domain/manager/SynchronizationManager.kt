package com.whisker.mrr.domain.manager

import io.reactivex.Completable

interface SynchronizationManager {
    fun fullDataSynchronization() : Completable
    fun imageSynchronization() : Completable
}