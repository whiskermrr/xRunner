package com.whisker.mrr.domain.source

import io.reactivex.Completable

interface SnapshotRemoteSource {

    fun saveSnapshotRemote(filePath: String, fileName: String) : Completable
}