package com.whisker.mrr.domain.model

data class Album(
    var id: Long,
    var title: String,
    var artist: String,
    var numberOfSongs: Int,
    var albumArt: String? = null
) {
    override fun toString(): String {
        return "id: $id artist: $artist album: $title, songs count: $numberOfSongs, art cover: $albumArt"
    }
}