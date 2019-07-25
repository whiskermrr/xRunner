package com.whisker.mrr.domain.repository

import io.reactivex.Completable

interface SnapshotRepository {
    fun saveSnapshot(bitmap: ByteArray, fileName: String) : Completable
    fun synchronizeSnapshots() : Completable
}