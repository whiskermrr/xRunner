package com.whisker.mrr.domain.model

data class Song(
    var id: Long? = null,
    var artist: String? = null,
    var title: String? = null,
    var data: String? = null,
    var displayName: String? = null,
    var duration: Long? = null,
    var albumId: Long? = null
) {
    override fun toString() : String {
        return "id: $id, artist: $artist, title: $title, data: $data, displayName: $displayName, duration: $duration, albumId: $albumId"
    }
}