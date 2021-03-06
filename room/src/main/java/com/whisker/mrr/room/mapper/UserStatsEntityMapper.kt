package com.whisker.mrr.room.mapper

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.room.model.UserStatsEntity

object UserStatsEntityMapper {

    fun transformFromEntity(entity: UserStatsEntity) : UserStats {
        val stats = UserStats(entity.userID)
        entity.averagePace?.let { stats.averagePace = it }
        entity.experience?.let { stats.experience = it }
        entity.totalDistance?.let { stats.totalDistance = it }
        entity.totalTime?.let { stats.totalTime = it }

        return stats
    }

    fun transformToEntity(stats: UserStats) : UserStatsEntity {
        val entity = UserStatsEntity(stats.userID)
        entity.averagePace = stats.averagePace
        entity.experience = stats.experience
        entity.totalDistance = stats.totalDistance
        entity.totalTime = stats.totalTime

        return entity
    }
}