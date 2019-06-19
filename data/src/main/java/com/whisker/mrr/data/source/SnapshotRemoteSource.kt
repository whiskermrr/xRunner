package com.whisker.mrr.data.source

import io.reactivex.Completable

interface SnapshotRemoteSource {

    fun saveSnapshotRemote(filePath: String, fileName: String) : Completable
    fun saveListOfSnapshotsRemote(files: MutableList<Pair<String, String>>): List<Completable>
}