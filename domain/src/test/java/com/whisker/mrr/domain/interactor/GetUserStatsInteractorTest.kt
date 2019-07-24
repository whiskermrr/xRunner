package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.exception.NoConnectivityException
import com.whisker.mrr.domain.common.scheduler.IOFlowableTransformer
import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.repository.UserRepository
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class GetUserStatsInteractorTest {

    private val mockUserRepository = mock<UserRepository>()

    private lateinit var transformer: FlowableTransformer<UserStats, UserStats>

    private lateinit var getUserStatsInteractor: GetUserStatsInteractor

    private val userStats = UserStats("1", 5.5f, 1900, 10000f, 9000L)

    private val updatedUserStats = UserStats("1", 5.4f, 2200, 12000f, 9500L)

    private val testSubscriber = TestSubscriber<UserStats>()

    private val connectivityException = NoConnectivityException()

    private val error = Throwable("DB access denied.")

    @Before
    fun setUp() {
        transformer = IOFlowableTransformer(Schedulers.trampoline())
        getUserStatsInteractor = GetUserStatsInteractor(transformer, mockUserRepository)
    }

    @Test
    fun getUserStatsSuccess() {
        val result = Flowable.just(userStats)

        whenever(mockUserRepository.getUserStats())
            .thenReturn(result)

        getUserStatsInteractor.getUserStats()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .await()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(userStats)
            .assertComplete()
    }

    @Test
    fun getUserStatsFailed() {
        val result = Flowable.error<UserStats>(error)

        whenever(mockUserRepository.getUserStats())
            .thenReturn(result)

        getUserStatsInteractor.getUserStats()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @Test
    fun getUserStatsSuccess2() {
        val result = Flowable.merge(
            Flowable.just(userStats),
            Flowable.just(updatedUserStats).delay(100, TimeUnit.MILLISECONDS)
        )

        whenever(mockUserRepository.getUserStats())
            .thenReturn(result)

        getUserStatsInteractor.getUserStats()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertValueCount(1)
            .await()
            .assertValueCount(2)
            .assertValues(userStats, updatedUserStats)
            .assertValueAt(1, updatedUserStats)
            .assertValueSequence(listOf(userStats, updatedUserStats))
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun getUserStatsSuccessThenFailed() {
        val result = Flowable.merge(
            Flowable.just(userStats),
            Flowable.error(connectivityException)
        )

        whenever(mockUserRepository.getUserStats())
            .thenReturn(result)

        getUserStatsInteractor.getUserStats()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertValueCount(1)
            .assertValue(userStats)
            .await()
            .assertError(connectivityException)
            .assertTerminated()
    }

    @Test
    fun getUserStatsFailedThenSuccess() {
        val result = Flowable.merge(
            Flowable.error(error),
            Flowable.just(updatedUserStats)
        )

        whenever(mockUserRepository.getUserStats())
            .thenReturn(result)

        getUserStatsInteractor.getUserStats()
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .await()
            .assertNoValues()
            .assertError(error)
            .assertTerminated()
    }

    @After
    fun tearDown() {
    }
}