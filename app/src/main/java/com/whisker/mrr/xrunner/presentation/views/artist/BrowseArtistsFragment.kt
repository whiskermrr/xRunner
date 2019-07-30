package com.whisker.mrr.xrunner.presentation.views.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.adapters.ArtistsAdapter
import com.whisker.mrr.xrunner.presentation.views.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_browse_artists.*

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
        return inflater.inflate(R.layout.fragment_browse_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BrowseArtistsViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)
        rvArtists.layoutManager = layoutManager
        rvArtists.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        rvArtists.adapter = artistsAdapter

        viewModel.getArtistList().observe(this, Observer { artists ->
            artistsAdapter.setArtists(artists)
        })

        viewModel.getIsSongListSet().observe(this, Observer { isSet ->
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