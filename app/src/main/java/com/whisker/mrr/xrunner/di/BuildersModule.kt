package com.whisker.mrr.xrunner.di

import com.whisker.mrr.infrastructure.SnapshotSyncService
import com.whisker.mrr.xrunner.presentation.views.MainActivity
import com.whisker.mrr.xrunner.presentation.views.challenge.AddChallengeDialogFragment
import com.whisker.mrr.xrunner.presentation.views.challenge.ChallengeFragment
import com.whisker.mrr.xrunner.presentation.views.history.PastRoutesFragment
import com.whisker.mrr.xrunner.presentation.views.login.LoginFragment
import com.whisker.mrr.xrunner.presentation.views.map.RunFragment
import com.whisker.mrr.xrunner.presentation.views.album.BrowseAlbumsFragment
import com.whisker.mrr.xrunner.presentation.views.artist.BrowseArtistsFragment
import com.whisker.mrr.xrunner.presentation.views.music.MusicBrowserFragment
import com.whisker.mrr.xrunner.presentation.views.music.MusicPlayerFragment
import com.whisker.mrr.xrunner.presentation.views.profile.UserProfileFragment
import com.whisker.mrr.xrunner.presentation.views.song.BrowseSongsFragment
import com.whisker.mrr.xrunner.presentation.views.summary.SummaryRunFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity() : MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindLoginFragment() : LoginFragment

    @ContributesAndroidInjector
    internal abstract fun bindRunFragment() : RunFragment

    @ContributesAndroidInjector
    internal abstract fun bindSummaryRunFragment() : SummaryRunFragment

    @ContributesAndroidInjector
    internal abstract fun bindPastRoutesFragment() : PastRoutesFragment

    @ContributesAndroidInjector
    internal abstract fun bindUserProfileFragment() : UserProfileFragment

    @ContributesAndroidInjector
    internal abstract fun bindChallengeFragment() : ChallengeFragment

    @ContributesAndroidInjector
    internal abstract fun bindSnapshotSyncService() : SnapshotSyncService

    @ContributesAndroidInjector
    internal abstract fun bindAddChallengeDialogFragment() : AddChallengeDialogFragment

    @ContributesAndroidInjector
    internal abstract fun bindMusicPlayerFragment() : MusicPlayerFragment

    @ContributesAndroidInjector
    internal abstract fun bindMusicBrowserFragment() : MusicBrowserFragment

    @ContributesAndroidInjector
    internal abstract fun bindBrowseAlbumsFragment() : BrowseAlbumsFragment

    @ContributesAndroidInjector
    internal abstract fun bindBrowseArtistsFragment() : BrowseArtistsFragment

    @ContributesAndroidInjector
    internal abstract fun bindBrowseSongsFragment() : BrowseSongsFragment
}