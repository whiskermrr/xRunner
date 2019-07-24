package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOCompletableTransformer
import com.whisker.mrr.domain.manager.LocationManager
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Test

class PauseTrackingInteractorTest {

    private val mockLocationManager = mock<LocationManager>()

    private lateinit var transformer: CompletableTransformer

    private lateinit var pauseTrackingInteractor: PauseTrackingInteractor

    @Before
    fun setUp() {
        transformer = IOCompletableTransformer(Schedulers.trampoline())
        pauseTrackingInteractor = PauseTrackingInteractor(transformer, mockLocationManager)
    }

    @Test
    fun pauseTrackingSuccess() {
        whenever(mockLocationManager.pauseTracking())
            .thenReturn(Completable.complete())

        pauseTrackingInteractor.pauseTracking()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun pauseTrackingFailed() {
        val error = Throwable("Permission denied.")

        whenever(mockLocationManager.pauseTracking())
            .thenReturn(Completable.error(error))

        pauseTrackingInteractor.pauseTracking()
            .test()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}