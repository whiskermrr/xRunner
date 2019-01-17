package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.Achievement
import io.reactivex.Completable
import io.reactivex.Single

interface AchievementsRepository {

    fun saveAchievement(userId: String, achievement: Achievement) : Completable
    fun updateAchievements(userId: String, achievements: List<Achievement>) : Completable
    fun getAchievements(userId: String) : Single<List<Achievement>>
    fun getActiveAchievements(userId: String) : Single<List<Achievement>>
}