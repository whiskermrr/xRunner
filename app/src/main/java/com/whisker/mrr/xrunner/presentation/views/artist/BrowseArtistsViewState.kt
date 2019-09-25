package com.whisker.mrr.xrunner.presentation.views.artist

import com.whisker.mrr.domain.model.Artist

sealed class BrowseArtistsViewState {
    class Error(val msg: String?) : BrowseArtistsViewState()
    class Artists(val artists: List<Artist>) : BrowseArtistsViewState()
}