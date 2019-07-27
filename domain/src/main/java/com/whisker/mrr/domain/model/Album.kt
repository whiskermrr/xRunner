package com.whisker.mrr.domain.model

data class Album(
    var id: Long? = null,
    var title: String? = null,
    var artist: String? = null,
    var numberOfSongs: Int? = null,
    var albumArt: String? = null
) {
    override fun toString(): String {
        return "id: $id artist: $artist album: $title, songs count: $numberOfSongs, art cover: $albumArt"
    }
}