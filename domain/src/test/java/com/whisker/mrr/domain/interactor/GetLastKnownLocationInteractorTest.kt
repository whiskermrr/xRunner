package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOMaybeTransformer
import com.whisker.mrr.domain.manager.LocationManager
import com.whisker.mrr.domain.model.Coords
import io.reactivex.Maybe
import io.reactivex.MaybeTransformer
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class GetLastKnownLocationInteractorTest {

    private val mockLocationManager = mock<LocationManager>()

    private lateinit var transformer: MaybeTransformer<Coords, Coords>

    private lateinit var getLastKnownLocationInteractor: GetLastKnownLocationInteractor

    @Before
    fun setUp() {
        transformer = IOMaybeTransformer(Schedulers.trampoline())
        getLastKnownLocationInteractor = GetLastKnownLocationInteractor(transformer, mockLocationManager)
    }

    @Test
    fun getLastKnownLocationSuccess() {
        val lastLocation = Coords(54.312, 46.989)

        whenever(mockLocationManager.getBestLastKnownLocation())
            .thenReturn(Maybe.just(lastLocation))

        getLastKnownLocationInteractor.maybe()
            .test()
            .await()
            .assertValue(lastLocation)
            .assertComplete()
    }

    @Test
    fun getLastKnownLocationEmpty() {
        whenever(mockLocationManager.getBestLastKnownLocation())
            .thenReturn(Maybe.empty())

        getLastKnownLocationInteractor.maybe()
            .test()
            .await()
            .assertNoValues()
            .assertComplete()
    }

    @Test
    fun getLastKnownLocationFailed() {
        val error = Throwable("Permission denied.")

        whenever(mockLocationManager.getBestLastKnownLocation())
            .thenReturn(Maybe.error(error))

        getLastKnownLocationInteractor.maybe()
            .test()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}