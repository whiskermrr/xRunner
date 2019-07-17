package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOSingleTransformer
import com.whisker.mrr.domain.model.Album
import com.whisker.mrr.domain.repository.MusicRepository
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test


class GetAlbumsInteractorTest {

    private val mockMusicRepository = mock<MusicRepository>()

    lateinit var transformer: SingleTransformer<List<Album>, List<Album>>

    lateinit var getAlbumsInteractor: GetAlbumsInteractor

    @Before
    fun setUp() {
        transformer = IOSingleTransformer(Schedulers.io())
        getAlbumsInteractor = GetAlbumsInteractor(transformer, mockMusicRepository)
    }

    @Test
    fun getAlbumsSuccess() {
        val albums = listOf(
            Album(1L, "while 1 < 2", "Deadmau5", 27, null),
            Album(1L, "Album Title Goes Here", "Deadmau5", 18, null),
            Album(1L, "Random Album Title", "Deadmau5", 10, null)
        )
        whenever(mockMusicRepository.getAlbums())
            .thenReturn(Single.just(albums))

        getAlbumsInteractor.getAlbums()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun getAlbumsEmptyList() {
        val albums = emptyList<Album>()

        whenever(mockMusicRepository.getAlbums())
            .thenReturn(Single.just(albums))

        getAlbumsInteractor.getAlbums()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun getAlbumsFailed() {
        val error = Throwable("Failed to fetch albums")

        whenever(mockMusicRepository.getAlbums())
            .thenReturn(Single.error(error))

        getAlbumsInteractor.getAlbums()
            .test()
            .await()
            .assertError(error)
    }

    @After
    fun tearDown() {
    }
}