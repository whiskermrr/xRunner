package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOSingleTransformer
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.ChallengeDifficulty
import com.whisker.mrr.domain.repository.ChallengeRepository
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test


class GetActiveChallengesInteractorTest {

    private val mockChallengeRepository = mock<ChallengeRepository>()

    lateinit var transformer: SingleTransformer<List<Challenge>, List<Challenge>>

    lateinit var getActiveChallengesInteractor: GetActiveChallengesInteractor

    @Before
    fun setUp() {
        transformer = IOSingleTransformer(Schedulers.trampoline())
        getActiveChallengesInteractor = GetActiveChallengesInteractor(transformer, mockChallengeRepository)
    }

    @Test
    fun getActiveChallengesSuccess() {
        val challenges = listOf(
            Challenge(1L, false, null, "Challenge1", 10000f, null, null, 0, ChallengeDifficulty.EASY, 1000),
            Challenge(2L, false, null, "Challenge1", 10000f, 5.5f, null, 0, ChallengeDifficulty.EASY, 2100),
            Challenge(3L, false, null, "Challenge1", 10000f, null, 20000, 0, ChallengeDifficulty.EASY, 12100)
        )

        whenever(mockChallengeRepository.getActiveChallengesSingle())
            .thenReturn(Single.just(challenges))

        getActiveChallengesInteractor.getChallenges()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun getActiveChallengesEmptyList() {
        val challenges = emptyList<Challenge>()

        whenever(mockChallengeRepository.getActiveChallengesSingle())
            .thenReturn(Single.just(challenges))

        getActiveChallengesInteractor.getChallenges()
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun getActiveChallengesFailed() {
        val error = Throwable("Database error")

        whenever(mockChallengeRepository.getActiveChallengesSingle())
            .thenReturn(Single.error(error))

        getActiveChallengesInteractor.getChallenges()
            .test()
            .await()
            .assertError(error)
    }

    @After
    fun tearDown() {
    }
}