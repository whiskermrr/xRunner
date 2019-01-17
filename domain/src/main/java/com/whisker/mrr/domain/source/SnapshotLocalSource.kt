package com.whisker.mrr.domain.source


interface SnapshotLocalSource {

    fun saveSnapshotLocal(bitmap: ByteArray, fileName: String) : String
    fun markSnapshotAsSent(fileName: String)
}