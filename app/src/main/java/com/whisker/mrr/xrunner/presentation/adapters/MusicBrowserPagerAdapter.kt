package com.whisker.mrr.xrunner.presentation.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.music.BrowseAlbumsFragment
import com.whisker.mrr.xrunner.presentation.views.music.BrowseArtistsFragment

class MusicBrowserPagerAdapter(private val context: Context, fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when(position) {
            0 -> BrowseArtistsFragment()
            1 -> BrowseAlbumsFragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> context.getString(R.string.tab_artists)
            1 -> context.getString(R.string.tab_albums)
            else -> null
        }
    }
}