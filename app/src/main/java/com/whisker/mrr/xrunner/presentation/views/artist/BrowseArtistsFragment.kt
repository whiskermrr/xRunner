package com.whisker.mrr.xrunner.presentation.views.artist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.adapters.ArtistsAdapter
import com.whisker.mrr.xrunner.presentation.views.base.BaseFragment
import com.whisker.mrr.xrunner.utils.TAG
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_browse_music.*

class BrowseArtistsFragment : BaseFragment() {

    private lateinit var viewModel: BrowseArtistsViewModel
    private lateinit var artistsAdapter: ArtistsAdapter
    private lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposables = CompositeDisposable()
        artistsAdapter = ArtistsAdapter()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BrowseArtistsViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)
        rvMusic.layoutManager = layoutManager
        rvMusic.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        rvMusic.adapter = artistsAdapter

        viewModel.getArtistList().observe(viewLifecycleOwner, Observer { viewState ->
            when(viewState) {
                is BrowseArtistsViewState.Artists -> {
                    val artists = viewState.artists
                    if(artists.isNotEmpty()) {
                        artistsAdapter.setArtists(artists)
                    } else {
                        tvMusicNoResult.visibility = View.VISIBLE
                    }
                }
                is BrowseArtistsViewState.Error -> {
                    // TODO: dialog showing error
                    Log.e(TAG(), viewState.msg ?: "Unknown Error")
                }
            }
        })

        viewModel.getIsSongListSet().observe(viewLifecycleOwner, Observer { isSet ->
            if(isSet) mainActivity.onBackPressed()
        })

        disposables.add(
            artistsAdapter.clickEvent()
                .subscribe({ artist ->
                    viewModel.setSongs(artist.id)
                }, Throwable::printStackTrace)
        )
    }
}