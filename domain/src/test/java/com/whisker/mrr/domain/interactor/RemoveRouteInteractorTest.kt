package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOCompletableTransformer
import com.whisker.mrr.domain.repository.RouteRepository
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong

class RemoveRouteInteractorTest {

    private val mockRouteRepository = mock<RouteRepository>()

    private lateinit var transformer: CompletableTransformer

    private lateinit var removeRouteInteractor: RemoveRouteInteractor

    @Before
    fun setUp() {
        transformer = IOCompletableTransformer(Schedulers.trampoline())
        removeRouteInteractor = RemoveRouteInteractor(transformer, mockRouteRepository)
    }

    @Test
    fun removeRouteSuccess() {
        whenever(mockRouteRepository.removeRoute(anyLong()))
            .thenReturn(Completable.complete())

        removeRouteInteractor.removeRoute(12L)
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun removeRouteFailed() {
        val error = Throwable("DB access denied.")

        whenever(mockRouteRepository.removeRoute(anyLong()))
            .thenReturn(Completable.error(error))

        removeRouteInteractor.removeRoute(11L)
            .test()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}