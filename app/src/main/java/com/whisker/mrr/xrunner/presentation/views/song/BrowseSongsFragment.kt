package com.whisker.mrr.xrunner.presentation.views.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.adapters.SongsAdapter
import com.whisker.mrr.xrunner.presentation.views.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_browse_songs.*

class BrowseSongsFragment : BaseFragment() {

    private lateinit var viewModel: BrowseSongsViewModel
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposables = CompositeDisposable()
        songsAdapter = SongsAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse_songs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BrowseSongsViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)
        rvSongs.layoutManager = layoutManager
        rvSongs.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        rvSongs.adapter = songsAdapter

        viewModel.getSongList().observe(this, Observer { songs ->
            songsAdapter.setSongs(songs)
        })

        viewModel.getIsSongListSet().observe(this, Observer { isSet ->
            if(isSet) mainActivity.onBackPressed()
        })

        disposables.add(
            songsAdapter.clickEvent()
                .subscribe({ position ->
                    viewModel.setSongs(songsAdapter.getSongs(), position)
                }, Throwable::printStackTrace)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
    }
}