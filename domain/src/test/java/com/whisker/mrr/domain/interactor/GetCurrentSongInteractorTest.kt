package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOFlowableTransformer
import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.model.Song
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before

import org.junit.Test
import java.util.concurrent.TimeUnit

class GetCurrentSongInteractorTest {

    private val mockMusicManager = mock<MusicManager>()

    private lateinit var transformer: FlowableTransformer<Song, Song>

    private lateinit var getCurrentSongInteractor: GetCurrentSongInteractor

    private val songs = listOf(
        Song(1L, "Deadmau5", "Avaritia", "12.12.2012", "Deadmau5 - Avaritia", 19820, 1L),
        Song(1L, "Deadmau5", "Phantoms Can’t Hang", "12.12.2012", "Deadmau5 - Phantoms Can’t Hang", 11820, 1L),
        Song(1L, "Deadmau5", "My Pet Coelacanth", "12.12.2012", "Deadmau5 - My Pet Coelacanth", 16220, 1L)
    )

    @Before
    fun setUp() {
        transformer = IOFlowableTransformer(Schedulers.trampoline())
        getCurrentSongInteractor = GetCurrentSongInteractor(transformer, mockMusicManager)
    }

    @Test
    fun getCurrentSongSuccess() {
        val currentSong = songs[0]
        whenever(mockMusicManager.currentSong())
            .thenReturn(Flowable.just(currentSong))

        getCurrentSongInteractor.getCurrentSong()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValueCount(1)
            .assertValues(currentSong)
    }

    @Test
    fun getCurrentSongSuccessAndThenNextSong() {
        val currentSong = songs[0]
        val nextSong = songs[1]
        val testSubscriber = TestSubscriber<Song>()

        val result = Flowable.merge(
            Flowable.just(currentSong),
            Flowable.just(nextSong).delay(100, TimeUnit.MILLISECONDS)
        )

        whenever(mockMusicManager.currentSong())
            .thenReturn(result)

        getCurrentSongInteractor.getCurrentSong()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertValues(currentSong)
            .await()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueSequence(listOf(currentSong, nextSong))
    }

    @Test
    fun getCurrentSongFailed() {
        val error = Throwable("DB Access Denied.")
        val testSubscriber = TestSubscriber<Song>()

        val result = Flowable.error<Song>(error)

        whenever(mockMusicManager.currentSong())
            .thenReturn(result)

        getCurrentSongInteractor.getCurrentSong()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .await()
            .assertNoValues()
            .assertError(error)
            .assertTerminated()
    }

    @Test
    fun getCurrentSongSuccessThenFailed() {
        val error = Throwable("DB Access Denied.")
        val testSubscriber = TestSubscriber<Song>()

        val result = Flowable.merge(
            Flowable.just(songs[0]),
            Flowable.error(error)
        )

        whenever(mockMusicManager.currentSong())
            .thenReturn(result)

        getCurrentSongInteractor.getCurrentSong()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .awaitCount(1)
            .assertValue(songs[0])
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}