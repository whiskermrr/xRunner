package com.whisker.mrr.xrunner.presentation.views.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.*
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MusicPlayerViewModel
@Inject constructor(
    private val getSongsInteractor: GetSongsInteractor,
    private val setSongsInteractor: SetSongsInteractor,
    private val playMusicInteractor: PlayMusicInteractor,
    private val nextSongInteractor: NextSongInteractor,
    private val previousSongInteractor: PreviousSongInteractor,
    private val pauseMusicInteractor: PauseMusicInteractor,
    private val stopMusicInteractor: StopMusicInteractor,
    private val getCurrentSongInteractor: GetCurrentSongInteractor,
    private val isMusicPlayingInteractor: IsMusicPlayingInteractor
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val currentSong = MutableLiveData<String>()
    private val isMusicPlaying = MutableLiveData<Boolean>()

    init {
        isMusicPlaying()
        subscribeToCurrentSong()
    }

    fun getMusic() {
        disposables.add(
            getSongsInteractor.getSongs()
                .flatMapCompletable {
                    if(it.isNotEmpty()) {
                        currentSong.postValue(it[0].displayName)
                    }
                    setSongsInteractor.setSongs(it)
                }
                .subscribe({
                    isMusicPlaying.postValue(false)
                }, Throwable::printStackTrace)
        )
    }

    private fun isMusicPlaying() {
        disposables.add(
            isMusicPlayingInteractor.isMusicPlaying()
                .subscribe({ isPlaying ->
                    isMusicPlaying.postValue(isPlaying)
                }, Throwable::printStackTrace)
        )
    }

    private fun subscribeToCurrentSong() {
        disposables.add(
            getCurrentSongInteractor.getCurrentSong()
                .subscribe({ song ->
                    currentSong.postValue(song.displayName)
                }, Throwable::printStackTrace)
        )
    }

    fun startPlayingMusic() {
        disposables.add(
            playMusicInteractor.playMusic()
                .subscribe({}, Throwable::printStackTrace)
        )
    }

    fun pausePlayingMusic() {
        disposables.add(
            pauseMusicInteractor.pauseMusic()
                .subscribe({}, Throwable::printStackTrace)
        )
    }

    fun nextSong() {
        disposables.add(
            nextSongInteractor.nextSong()
                .subscribe({}, Throwable::printStackTrace)
        )
    }

    fun previousSong() {
        disposables.add(
            previousSongInteractor.previousSong()
                .subscribe({}, Throwable::printStackTrace)
        )
    }

    fun stopMusic() {
        stopMusicInteractor.execute()
    }

    fun getCurrentSong() = currentSong
    fun getIsMusicPlaying() = isMusicPlaying

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}