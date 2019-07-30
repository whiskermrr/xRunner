package com.whisker.mrr.xrunner.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whisker.mrr.xrunner.presentation.views.challenge.AddChallengeViewModel
import com.whisker.mrr.xrunner.presentation.views.challenge.ChallengeViewModel
import com.whisker.mrr.xrunner.presentation.views.history.PastRoutesViewModel
import com.whisker.mrr.xrunner.presentation.views.login.LoginViewModel
import com.whisker.mrr.xrunner.presentation.views.map.RunViewModel
import com.whisker.mrr.xrunner.presentation.views.album.BrowseAlbumsViewModel
import com.whisker.mrr.xrunner.presentation.views.artist.BrowseArtistsViewModel
import com.whisker.mrr.xrunner.presentation.views.music.MusicPlayerViewModel
import com.whisker.mrr.xrunner.presentation.views.profile.UserProfileViewModel
import com.whisker.mrr.xrunner.presentation.views.song.BrowseSongsViewModel
import com.whisker.mrr.xrunner.presentation.views.summary.SummaryRunViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RunViewModel::class)
    internal abstract fun bindMapViewModel(RunViewModel: RunViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SummaryRunViewModel::class)
    internal abstract fun bindSummaryRunViewModel(SummaryRunViewModel: SummaryRunViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PastRoutesViewModel::class)
    internal abstract fun bindPastRoutesViewModel(pastRoutesViewModel: PastRoutesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    internal abstract fun  bindUserProfileViewModel(userProfileViewModel: UserProfileViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChallengeViewModel::class)
    internal abstract fun bindChallengeViewModel(challengeViewModel: ChallengeViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddChallengeViewModel::class)
    internal abstract fun bindAddChallengeViewModel(addChallengeViewModel: AddChallengeViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MusicPlayerViewModel::class)
    internal abstract fun bindMusicPlayerViewModel(musicPlayerViewModel: MusicPlayerViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BrowseAlbumsViewModel::class)
    internal abstract fun bindBrowseAlbumsViewModel(browseAlbumsViewModel: BrowseAlbumsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BrowseArtistsViewModel::class)
    internal abstract fun bindBrowseArtistsViewModel(browseArtistsViewModel: BrowseArtistsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BrowseSongsViewModel::class)
    internal abstract fun bindBrowseSongsViewModel(browseSongsViewModel: BrowseSongsViewModel) : ViewModel
}