package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Achievement
import com.whisker.mrr.domain.repository.AchievementsRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class UpdateAchievementsInteractor(
    transformer: CompletableTransformer,
    private val authSource: AuthSource,
    private val achievementsRepository: AchievementsRepository
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_ACHIEVEMENTS_LIST = "param_achievements_list"
    }

    fun updateAchievements(achievements: List<Achievement>) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_ACHIEVEMENTS_LIST] = achievements
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val achievements = data?.get(PARAM_ACHIEVEMENTS_LIST)

        achievements?.let {
            return if(achievements is List<*>)
                authSource.getUserId()
                .flatMapCompletable {  userId ->
                    achievementsRepository.updateAchievements(userId, achievements.filterIsInstance<Achievement>())
                } else {
                return Completable.error(ClassCastException("Cannot cost parameter @achievements to List<Achievements>"))
            }
        } ?: return Completable.error(IllegalArgumentException("Parameter @achievements must be provided."))
    }
}