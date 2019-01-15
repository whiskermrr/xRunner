package com.whisker.mrr.xrunner.domain.interactor

import com.whisker.mrr.xrunner.domain.model.Achievement
import com.whisker.mrr.xrunner.domain.repository.AchievementsRepository
import com.whisker.mrr.xrunner.domain.source.AuthSource
import com.whisker.mrr.xrunner.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetAchievementsInteractor(
    transformer: SingleTransformer<List<Achievement>, List<Achievement>>,
    private val authSource: AuthSource,
    private val achievementsRepository: AchievementsRepository
) : SingleUseCase<List<Achievement>>(transformer) {

    companion object {
        const val PARAM_ACTIVE = "param_active"
    }

    fun getAchievements(active: Boolean = false) : Single<List<Achievement>> {
        val data = HashMap<String, Any>()
        data[PARAM_ACTIVE] = active
        return single(data)
    }

    override fun createSingle(data: Map<String, Any>?): Single<List<Achievement>> {
        val active = data?.get(PARAM_ACTIVE)

        active?.let {
            return authSource.getUserId()
                .flatMap { userId ->
                    if((active as Boolean)) {
                        return@flatMap achievementsRepository.getActiveAchievements(userId)
                    } else {
                        return@flatMap achievementsRepository.getAchievements(userId)
                    }
                }
        }
    }
}