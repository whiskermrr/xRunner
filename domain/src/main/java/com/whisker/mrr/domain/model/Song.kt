package com.whisker.mrr.domain.model

data class Song(
    private var id: Long? = null,
    private var artist: String? = null,
    private var title: String? = null,
    private var data: String? = null,
    private var displayName: String? = null,
    private var duration: Long? = null,
    private var albumId: Long? = null
) {
    override fun toString() : String {
        return "id: $id, artist: $artist, title: $title, data: $data, displayName: $displayName, duration: $duration, albumId: $albumId"
    }
}