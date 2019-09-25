package com.whisker.mrr.xrunner.presentation.views.album

import com.whisker.mrr.domain.model.Album

sealed class BrowseAlbumsViewState {
    class Error(val msg: String?) : BrowseAlbumsViewState()
    class Albums(val albums: List<Album>) : BrowseAlbumsViewState()
}