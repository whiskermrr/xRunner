package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Achievement
import com.whisker.mrr.domain.repository.AchievementsRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveAchievementInteractor(
    transformer: CompletableTransformer,
    private val authSource: AuthSource,
    private val achievementsRepository: AchievementsRepository
) : CompletableUseCase(transformer) {

    companion object {
        const val PARAM_ACHIEVEMENT = "param_achievement"
    }

    fun saveAchievement(achievement: Achievement) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_ACHIEVEMENT] = achievement
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val achievement = data?.get(PARAM_ACHIEVEMENT)

        achievement?.let {
            return authSource.getUserId()
                .flatMapCompletable {  userId ->
                    achievementsRepository.saveAchievement(userId, achievement as Achievement)
                }
        } ?: return Completable.error(IllegalArgumentException("Argument @achievement must be provided."))
    }
}