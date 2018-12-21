package com.whisker.mrr.xrunner.domain.source

import io.reactivex.Completable

interface SnapshotRemoteSource {

    fun saveSnapshotRemote(filePath: String, fileName: String) : Completable
}