package com.whisker.mrr.domain.model

data class Album(
    private var id: Long? = null,
    private var album: String? = null,
    private var artist: String? = null,
    private var numberOfSongs: Int? = null,
    private var albumArt: String? = null
) {
    override fun toString(): String {
        return "id: $id artist: $artist album: $album, songs count: $numberOfSongs, art cover: $albumArt"
    }
}