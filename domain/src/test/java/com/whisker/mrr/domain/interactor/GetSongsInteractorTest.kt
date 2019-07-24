package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOSingleTransformer
import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.domain.repository.MusicRepository
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Test

class GetSongsInteractorTest {

    private val mockMusicRepository = mock<MusicRepository>()

    private lateinit var transformer: SingleTransformer<List<Song>, List<Song>>

    private lateinit var getSongsInteractor: GetSongsInteractor

    private val testObserver = TestObserver<List<Song>>()

    @Before
    fun setUp() {
        transformer = IOSingleTransformer(Schedulers.trampoline())
        getSongsInteractor = GetSongsInteractor(transformer, mockMusicRepository)
    }

    private val songs = listOf(
        Song(1L, "Deadmau5", "Avaritia", "12.12.2012", "Deadmau5 - Avaritia", 19820, 1L),
        Song(1L, "Deadmau5", "Phantoms Can’t Hang", "12.12.2012", "Deadmau5 - Phantoms Can’t Hang", 11820, 1L),
        Song(1L, "Deadmau5", "My Pet Coelacanth", "12.12.2012", "Deadmau5 - My Pet Coelacanth", 16220, 1L)
    )

    @Test
    fun getSongsSuccess() {
        val result = Single.just(songs)

        whenever(mockMusicRepository.getSongs())
            .thenReturn(result)

        getSongsInteractor.getSongs()
            .subscribe(testObserver)

        testObserver.assertNoValues()
            .await()
            .assertNoErrors()
            .assertValue(songs)
            .assertComplete()
    }

    @Test
    fun getSongsEmptyList() {
        val result = Single.just(emptyList<Song>())

        whenever(mockMusicRepository.getSongs())
            .thenReturn(result)

        getSongsInteractor.getSongs()
            .subscribe(testObserver)

        testObserver.assertNoValues()
            .await()
            .assertNoErrors()
            .assertValue(emptyList())
            .assertComplete()
    }

    @Test
    fun getSongsError() {
        val error = Throwable("DB access denied.")
        val result = Single.error<List<Song>>(error)

        whenever(mockMusicRepository.getSongs())
            .thenReturn(result)

        getSongsInteractor.getSongs()
            .subscribe(testObserver)

        testObserver.assertNoValues()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}