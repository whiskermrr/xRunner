package com.whisker.mrr.room.mapper

import com.whisker.mrr.domain.model.ChallengeProgress
import com.whisker.mrr.room.model.ChallengeProgressEntity

object ChallengeProgressEntityMapper {

    fun transformListFromEntities(entityList: List<ChallengeProgressEntity>) : List<ChallengeProgress> {
        val challengeProgressList = mutableListOf<ChallengeProgress>()
        for(entity in entityList) {
            challengeProgressList.add(transformFromEntity(entity))
        }
        return challengeProgressList
    }

    fun transofrmListToEntities(challengeProgressList: List<ChallengeProgress>) : List<ChallengeProgressEntity> {
        val entityList =  mutableListOf<ChallengeProgressEntity>()
        for(progress in challengeProgressList) {
            entityList.add(transofrmToEntity(progress))
        }
        return entityList
    }

    fun transformFromEntity(entity: ChallengeProgressEntity) : ChallengeProgress {
        val progress = ChallengeProgress(entity.challengeID)
        entity.timeProgress?.let { progress.timeProgress = it }
        entity.distanceProgress?.let { progress.distanceProgress = it }
        return progress
    }

    fun transofrmToEntity(progress: ChallengeProgress) : ChallengeProgressEntity {
        val entity = ChallengeProgressEntity(progress.challengeID)
        progress.timeProgress?.let { entity.timeProgress = it }
        progress.distanceProgress?.let { entity.distanceProgress = it }
        return entity
    }
}