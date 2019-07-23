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

import org.junit.Assert.*
import org.junit.Test

class ResumeTrackingInteractorTest {

    private val mockLocationManager = mock<LocationManager>()

    private lateinit var transformer: CompletableTransformer

    private lateinit var resumeTrackingInteractor: ResumeTrackingInteractor

    @Before
    fun setUp() {
        transformer = IOCompletableTransformer(Schedulers.trampoline())
        resumeTrackingInteractor = ResumeTrackingInteractor(transformer, mockLocationManager)
    }

    @Test
    fun resumeTrackingSuccess() {
        whenever(mockLocationManager.resumeTracking())
            .thenReturn(Completable.complete())

        resumeTrackingInteractor.resumeTracking()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun resumeTrackingFailed() {
        val error = Throwable("Permission denied.")

        whenever(mockLocationManager.resumeTracking())
            .thenReturn(Completable.error(error))

        resumeTrackingInteractor.resumeTracking()
            .test()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}