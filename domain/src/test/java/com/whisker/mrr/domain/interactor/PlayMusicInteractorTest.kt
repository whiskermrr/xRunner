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

class PlayMusicInteractorTest {

    private val mockMusicManager = mock<MusicManager>()

    private lateinit var transformer: CompletableTransformer

    private lateinit var playMusicInteractor: PlayMusicInteractor

    @Before
    fun setUp() {
        transformer = IOCompletableTransformer(Schedulers.trampoline())
        playMusicInteractor = PlayMusicInteractor(transformer, mockMusicManager)
    }

    @Test
    fun playMusicSuccess() {
        whenever(mockMusicManager.play())
            .thenReturn(Completable.complete())

        playMusicInteractor.playMusic()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun playMusicFailed() {
        val error = Throwable("Permission denied.")
        whenever(mockMusicManager.play())
            .thenReturn(Completable.error(error))

        playMusicInteractor.playMusic()
            .test()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}