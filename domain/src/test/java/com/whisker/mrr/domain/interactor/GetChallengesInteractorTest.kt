package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.exception.NoConnectivityException
import com.whisker.mrr.domain.common.scheduler.IOFlowableTransformer
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.ChallengeDifficulty
import com.whisker.mrr.domain.repository.ChallengeRepository
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before

import org.junit.Test
import java.util.concurrent.TimeUnit

class GetChallengesInteractorTest {

    private val mockChallengeRepository = mock<ChallengeRepository>()

    private lateinit var transformer: FlowableTransformer<List<Challenge>, List<Challenge>>

    private lateinit var getChallengesInteractor: GetChallengesInteractor

    private val challenges = listOf(
        Challenge(1L, false, null, "Challenge1", 10000f, null, null, 0, ChallengeDifficulty.EASY, 1000),
        Challenge(2L, false, null, "Challenge1", 10000f, 5.5f, null, 0, ChallengeDifficulty.EASY, 2100),
        Challenge(3L, false, null, "Challenge1", 10000f, null, 20000, 0, ChallengeDifficulty.EASY, 12100)
    )

    private val remoteChallenges = listOf(
        Challenge(4L, false, null, "Challenge1", 20000f, null, 10000, 0, ChallengeDifficulty.EASY, 18100)
    )

    @Before
    fun setUp() {
        transformer = IOFlowableTransformer(Schedulers.trampoline())
        getChallengesInteractor = GetChallengesInteractor(transformer, mockChallengeRepository)
    }

    @Test
    fun getActiveChallengesSuccess() {
        val testSubscriber = TestSubscriber<List<Challenge>>()

        whenever(mockChallengeRepository.getActiveChallenges())
            .thenReturn(Flowable.fromIterable(listOf(challenges, remoteChallenges)))


        getChallengesInteractor.getChallenges(true)
            .subscribe(testSubscriber)

        testSubscriber
            .await()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueSequence(listOf(challenges, remoteChallenges))
    }

    @Test
    fun getActiveChallengesSuccess2() {
        val testSubscriber = TestSubscriber<List<Challenge>>()
        val result = Flowable.merge(
            Flowable.just(challenges),
            Flowable.just(remoteChallenges).delay(100, TimeUnit.MILLISECONDS)
        )

        whenever(mockChallengeRepository.getActiveChallenges())
            .thenReturn(result)

        getChallengesInteractor.getChallenges(true)
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertValueCount(1)
            .await()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueSequence(listOf(challenges, remoteChallenges))
    }

    @Test
    fun getChallengesSuccess() {
        val testSubscriber = TestSubscriber<List<Challenge>>()

        whenever(mockChallengeRepository.getChallenges())
            .thenReturn(Flowable.fromIterable(listOf(challenges, remoteChallenges)))


        getChallengesInteractor.getChallenges(false)
            .subscribe(testSubscriber)

        testSubscriber
            .await()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueSequence(listOf(challenges, remoteChallenges))
    }

    @Test
    fun getChallengesSuccess2() {
        val testSubscriber = TestSubscriber<List<Challenge>>()
        val result = Flowable.merge(
            Flowable.just(challenges),
            Flowable.just(remoteChallenges).delay(100, TimeUnit.MILLISECONDS)
        )

        whenever(mockChallengeRepository.getChallenges())
            .thenReturn(result)

        getChallengesInteractor.getChallenges(false)
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertValueCount(1)
            .await()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueSequence(listOf(challenges, remoteChallenges))
    }

    @Test
    fun getActiveChallengesSuccessThenFailed() {
        val testSubscriber = TestSubscriber<List<Challenge>>()
        val error = NoConnectivityException()
        val result = Flowable.just(challenges)
            .concatWith(Flowable.error<List<Challenge>>(error).delay(100, TimeUnit.MILLISECONDS))


        whenever(mockChallengeRepository.getActiveChallenges())
            .thenReturn(result)

        getChallengesInteractor.getChallenges(true)
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertValueCount(1)
            .await()
            .assertError(error)
            .assertTerminated()
    }

    @Test
    fun getChallengesSuccessThenFailed() {
        val testSubscriber = TestSubscriber<List<Challenge>>()
        val noConnectivityException = NoConnectivityException()
        val result = Flowable.just(challenges)
            .concatWith(Flowable.error<List<Challenge>>(noConnectivityException).delay(100, TimeUnit.MILLISECONDS))


        whenever(mockChallengeRepository.getChallenges())
            .thenReturn(result)

        getChallengesInteractor.getChallenges(false)
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .assertNotComplete()
            .awaitCount(1)
            .assertValueCount(1)
            .await()
            .assertError(noConnectivityException)
            .assertTerminated()
    }

    @Test
    fun getActiveChallengesFailedFirst() {
        val testSubscriber = TestSubscriber<List<Challenge>>()
        val error = Throwable("DB access denied.")
        val result = Flowable.error<List<Challenge>>(error)
            .concatWith(Flowable.just(remoteChallenges).delay(100, TimeUnit.MILLISECONDS))

        whenever(mockChallengeRepository.getActiveChallenges())
            .thenReturn(result)

        getChallengesInteractor.getChallenges(true)
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .await()
            .assertNoValues()
            .assertError(error)
            .assertTerminated()
    }

    @Test
    fun getChallengesFailedFirst() {
        val testSubscriber = TestSubscriber<List<Challenge>>()
        val error = Throwable("DB access denied.")
        val result = Flowable.error<List<Challenge>>(error)
            .concatWith(Flowable.just(remoteChallenges).delay(100, TimeUnit.MILLISECONDS))

        whenever(mockChallengeRepository.getChallenges())
            .thenReturn(result)

        getChallengesInteractor.getChallenges(false)
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .await()
            .assertNoValues()
            .assertError(error)
            .assertTerminated()
    }

    @Test
    fun getActiveChallengesFailedBoth() {
        val testSubscriber = TestSubscriber<List<Challenge>>()
        val error = Throwable("DB access denied.")
        val noConnectivityException = NoConnectivityException()
        val result = Flowable.error<List<Challenge>>(error)
            .concatWith(Flowable.error<List<Challenge>>(noConnectivityException).delay(100, TimeUnit.MILLISECONDS))

        whenever(mockChallengeRepository.getActiveChallenges())
            .thenReturn(result)

        getChallengesInteractor.getChallenges(true)
            .subscribe(testSubscriber)

        testSubscriber.assertNoValues()
            .await()
            .assertNoValues()
            .assertError(error)
            .assertTerminated()
    }

    @Test
    fun getChallengesFailedBoth() {
        val testSubscriber = TestSubscriber<List<Challenge>>()
        val error = Throwable("DB access denied.")
        val noConnectivityException = NoConnectivityException()
        val result = Flowable.error<List<Challenge>>(error)
            .concatWith(Flowable.error<List<Challenge>>(noConnectivityException).delay(100, TimeUnit.MILLISECONDS))

        whenever(mockChallengeRepository.getChallenges())
            .thenReturn(result)

        getChallengesInteractor.getChallenges(false)
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