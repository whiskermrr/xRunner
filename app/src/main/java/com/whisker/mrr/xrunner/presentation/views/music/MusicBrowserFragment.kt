package com.whisker.mrr.xrunner.presentation.views.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.adapters.MusicBrowserPagerAdapter
import com.whisker.mrr.xrunner.presentation.views.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_music_browser.*

class MusicBrowserFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.toolbar.title = getString(R.string.title_music)
        mainActivity.hideBottomNavigation()
        musicViewPager.adapter = MusicBrowserPagerAdapter(requireContext(), activity?.supportFragmentManager)
        musicTabLayout.setupWithViewPager(musicViewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.showBottomNavigation()
    }
}