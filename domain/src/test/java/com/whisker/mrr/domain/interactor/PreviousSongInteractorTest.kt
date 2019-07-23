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

import org.junit.Assert.*
import org.junit.Test

class PreviousSongInteractorTest {

    private val mockMusicManager = mock<MusicManager>()

    private lateinit var transformer: CompletableTransformer

    private lateinit var previousSongInteractor: PreviousSongInteractor

    @Before
    fun setUp() {
        transformer = IOCompletableTransformer(Schedulers.io())
        previousSongInteractor = PreviousSongInteractor(transformer, mockMusicManager)
    }

    @Test
    fun previousSongSuccess() {
        whenever(mockMusicManager.previousSong())
            .thenReturn(Completable.complete())

        previousSongInteractor.previousSong()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun previousSongFailed() {
        val error = Throwable("Permission denied.")

        whenever(mockMusicManager.previousSong())
            .thenReturn(Completable.error(error))

        previousSongInteractor.previousSong()
            .test()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}