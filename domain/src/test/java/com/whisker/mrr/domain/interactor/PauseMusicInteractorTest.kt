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

class PauseMusicInteractorTest {

    private val mockMusicManager = mock<MusicManager>()

    private lateinit var transformer: CompletableTransformer

    private lateinit var pauseMusicInteractor: PauseMusicInteractor

    @Before
    fun setUp() {
        transformer = IOCompletableTransformer(Schedulers.io())
        pauseMusicInteractor = PauseMusicInteractor(transformer, mockMusicManager)
    }

    @Test
    fun pauseMusicSuccess() {
        whenever(mockMusicManager.pause())
            .thenReturn(Completable.complete())

        pauseMusicInteractor.pauseMusic()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun pauseMusicFailed() {
        val error = Throwable("Permission denied.")
        whenever(mockMusicManager.pause())
            .thenReturn(Completable.error(error))

        pauseMusicInteractor.pauseMusic()
            .test()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}