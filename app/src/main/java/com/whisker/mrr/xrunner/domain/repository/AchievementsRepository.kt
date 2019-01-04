package com.whisker.mrr.xrunner.domain.repository

import com.whisker.mrr.xrunner.domain.model.Achievement
import com.whisker.mrr.xrunner.domain.model.RouteStatsEntity
import io.reactivex.Completable
import io.reactivex.Single

interface AchievementsRepository {

    fun saveAchievement(userId: String, achievement: Achievement) : Completable
    fun updateAchievements(userId: String, stats: RouteStatsEntity) : Completable
    fun getAchievements(userId: String) : Single<List<Achievement>>
}