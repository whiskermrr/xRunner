package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.common.ChallengeUtils
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.RouteStats
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class UpdateChallengesInteractor(
    transformer: SingleTransformer<List<Challenge>, List<Challenge>>,
    private val challengeRepository: ChallengeRepository
) : SingleUseCase<List<Challenge>>(transformer) {

    companion object {
        const val PARAM_ROUTE_STATS = "param_route_stats"
    }

    fun updateChallenges(routeStats: RouteStats) : Single<List<Challenge>> {
        val data = HashMap<String, Any>()
        data[PARAM_ROUTE_STATS] = routeStats
        return single(data)
    }

    override fun createSingle(data: Map<String, Any>?): Single<List<Challenge>> {
        val param = data?.get(PARAM_ROUTE_STATS) as RouteStats?
        param?.let {stats ->
            return challengeRepository.getActiveChallengesSingle()
                .map { ChallengeUtils.getSelectedChallenges(stats, it) }
                .map { ChallengeUtils.updateChallengesProgress(stats, it) }
                .flatMap { updatedChallenges ->
                    challengeRepository.updateChallenges(updatedChallenges)
                        .onErrorResumeNext {
                            val progressList = ChallengeUtils.getChallengesProgress(stats, updatedChallenges)
                            challengeRepository.saveChallengesProgressListLocally(progressList)
                        }
                        .andThen(Single.just(updatedChallenges))
                }
        } ?: return Single.error(IllegalArgumentException("Parameter @routeStats must be provided."))
    }
}