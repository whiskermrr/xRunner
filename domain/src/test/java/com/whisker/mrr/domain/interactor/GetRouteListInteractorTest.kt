package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.exception.NoConnectivityException
import com.whisker.mrr.domain.common.scheduler.IOFlowableTransformer
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.domain.repository.RouteRepository
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before

import org.junit.Test
import java.util.concurrent.TimeUnit

class GetRouteListInteractorTest {

    private val mockRouteRepository = mock<RouteRepository>()

    private lateinit var transformer: FlowableTransformer<List<RouteHolder>, List<RouteHolder>>

    private lateinit var getRouteListInteractor: GetRouteListInteractor

    private val testSubscriber = TestSubscriber<List<RouteHolder>>()

    private val routesOne = listOf(
        Route("Friday Run", 1L, listOf(), RouteStats(), 123123123213L),
        Route("Saturday Run", 2L, listOf(), RouteStats(), 232131211131L),
        Route("Monday Run", 3L, listOf(), RouteStats(), 133323131213L)
    )

    private val routesTwo = listOf(
        Route("Friday Run 2", 4L, listOf(), RouteStats(), 123123123213L),
        Route("Friday Morning Run ", 5L, listOf(), RouteStats(), 123123123213L)
    )

    private val routesThree = listOf(
        Route("Friday Midnight Run", 6L, listOf(), RouteStats(), 123123123213L),
        Route("Monday Night Run", 7L, listOf(), RouteStats(), 123123123213L)
    )

    private val routeHolderOne = RouteHolder(1L, 21000f, 19000L, routesOne)

    private val routeHolderTwo = RouteHolder(2L, 20010f, 8000L, routesTwo)

    private val routeHolderThree = RouteHolder(3L, 21010f, 8800L, routesThree)

    @Before
    fun setUp() {
        transformer = IOFlowableTransformer(Schedulers.trampoline())
        getRouteListInteractor = GetRouteListInteractor(transformer, mockRouteRepository)
    }

    @Test
    fun getRouteListSuccess() {
        val resultValue = listOf(routeHolderOne, routeHolderTwo, routeHolderThree)
        val result = Flowable.just(resultValue)

        whenever(mockRouteRepository.getRouteList())
            .thenReturn(result)

        getRouteListInteractor.flowable()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .awaitCount(1)
            .assertNoErrors()
            .assertValue(resultValue)
            .await()
            .assertValue(resultValue)
            .assertComplete()

    }


    @Test
    fun getRouteListSuccess2() {
        val resultValueOne = listOf(routeHolderOne, routeHolderTwo)
        val resultValueTwo = listOf(routeHolderOne, routeHolderTwo, routeHolderThree)
        val result = Flowable.merge(
            Flowable.just(resultValueOne),
            Flowable.just(resultValueTwo).delay(100, TimeUnit.MILLISECONDS)
        )

        whenever(mockRouteRepository.getRouteList())
            .thenReturn(result)

        getRouteListInteractor.getRoutes()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertValueCount(1)
            .assertValue(resultValueOne)
            .await()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(1, resultValueTwo)
            .assertValueSequence(listOf(resultValueOne, resultValueTwo))
    }

    @Test
    fun getRouteListSuccessThenEmpty() {
        val resultValue = listOf(routeHolderOne, routeHolderTwo)

        val result = Flowable.merge(
            Flowable.just(resultValue),
            Flowable.empty<List<RouteHolder>>().delay(100, TimeUnit.MILLISECONDS)
        )

        whenever(mockRouteRepository.getRouteList())
            .thenReturn(result)

        getRouteListInteractor.getRoutes()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertNotComplete()
            .assertValueCount(1)
            .assertValue(resultValue)
            .await()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValueSequence(listOf(resultValue))
            .assertComplete()
    }

    @Test
    fun getRouteListSuccessThenError() {
        val resultValue = listOf(routeHolderOne, routeHolderTwo, routeHolderThree)
        val connectivityException = NoConnectivityException()
        val result = Flowable.merge(
            Flowable.just(resultValue),
            Flowable.error<List<RouteHolder>>(connectivityException).delay(100, TimeUnit.MILLISECONDS)
        )

        whenever(mockRouteRepository.getRouteList())
            .thenReturn(result)

        getRouteListInteractor.getRoutes()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertNotComplete()
            .assertValueCount(1)
            .assertValue(resultValue)
            .await()
            .assertError(connectivityException)
            .assertTerminated()
    }

    @Test
    fun getRouteListFailedThenSuccess() {
        val resultValue = listOf(routeHolderOne, routeHolderTwo, routeHolderThree)
        val connectivityException = NoConnectivityException()
        val result = Flowable.merge(
            Flowable.error<List<RouteHolder>>(connectivityException),
            Flowable.just(resultValue).delay(100, TimeUnit.MILLISECONDS)
        )

        whenever(mockRouteRepository.getRouteList())
            .thenReturn(result)

        getRouteListInteractor.getRoutes()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .await()
            .assertNoValues()
            .assertError(connectivityException)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}