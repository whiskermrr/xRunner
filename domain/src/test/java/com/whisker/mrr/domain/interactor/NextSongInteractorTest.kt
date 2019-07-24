package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOCompletableTransformer
import com.whisker.mrr.domain.manager.MusicManager
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Test

class NextSongInteractorTest {

    private val mockMusicManager = mock<MusicManager>()

    private lateinit var transformer: CompletableTransformer

    private lateinit var nextSongInteractor: NextSongInteractor

    @Before
    fun setUp() {
        transformer = IOCompletableTransformer(Schedulers.io())
        nextSongInteractor = NextSongInteractor(transformer, mockMusicManager)
    }

    @Test
    fun nextSongSuccess() {
        whenever(mockMusicManager.nextSong())
            .thenReturn(Completable.complete())

        nextSongInteractor.nextSong()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun nextSongFailed() {
        val error = Throwable("Permission denied.")
        whenever(mockMusicManager.nextSong())
            .thenReturn(Completable.error(error))

        nextSongInteractor.nextSong()
            .test()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}