package com.whisker.mrr.room.mapper

import com.whisker.mrr.domain.model.UserStatsProgress
import com.whisker.mrr.room.model.UserStatsProgressEntity

object UserStatsProgressMapper {

    fun transformListFromEntities(entityList: List<UserStatsProgressEntity>) : List<UserStatsProgress> {
        val statsList = mutableListOf<UserStatsProgress>()
        for(entity in entityList) {
            statsList.add(transformFromEntity(entity))
        }

        return statsList
    }

    fun transformListToEntities(statsList: List<UserStatsProgress>) : List<UserStatsProgressEntity> {
        val entityList = mutableListOf<UserStatsProgressEntity>()
        for(stats in statsList) {
            entityList.add(trasnformToEntity(stats))
        }

        return entityList
    }

    fun transformFromEntity(entityProgress: UserStatsProgressEntity) : UserStatsProgress {
        val statsProgress = UserStatsProgress()
        statsProgress.distanceProgress = entityProgress.distanceProgress
        statsProgress.timeProgress = entityProgress.timeProgress
        statsProgress.expProgress = entityProgress.expProgress

        return statsProgress
    }

    fun trasnformToEntity(statsProgress: UserStatsProgress) : UserStatsProgressEntity {
        val entityProgress = UserStatsProgressEntity()
        entityProgress.distanceProgress = statsProgress.distanceProgress
        entityProgress.timeProgress = statsProgress.timeProgress
        entityProgress.expProgress = statsProgress.expProgress

        return entityProgress
    }
}