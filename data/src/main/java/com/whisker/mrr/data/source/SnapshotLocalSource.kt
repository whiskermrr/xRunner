package com.whisker.mrr.data.source


interface SnapshotLocalSource {

    fun saveSnapshotLocal(bitmap: ByteArray, fileName: String) : String
    fun markSnapshotAsSent(fileName: String)
    fun getNotSentSnapshotsPaths(): List<Pair<String, String>>
    fun replaceNotSentSnapshotsPaths(newPaths: Set<String>)
}