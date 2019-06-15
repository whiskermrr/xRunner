package com.whisker.mrr.room.mapper

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.room.model.ChallengeEntity

object ChallengeEntityMapper {

    fun transformListFromEntities(challengeEntities: List<ChallengeEntity>) : List<Challenge> {
        val challenges = mutableListOf<Challenge>()
        for(entity in challengeEntities) {
            challenges.add(transformFromEntity(entity))
        }
        return challenges
    }

    fun transformListToEntities(challenges: List<Challenge>) : List<ChallengeEntity> {
        val entities = mutableListOf<ChallengeEntity>()
        for(challenge in challenges) {
            entities.add(transformToEntity(challenge))
        }
        return entities
    }

    fun transformFromEntity(entity: ChallengeEntity) : Challenge {
        val challenge = Challenge()
        entity.id?.let { challenge.id = it }
        entity.isFinished?.let { challenge.isFinished = it }
        entity.deadline?.let { challenge.deadline = it }
        entity.title?.let { challenge.title = it }
        entity.distance?.let { challenge.distance = it }
        entity.speed?.let { challenge.speed = it }
        entity.time?.let { challenge.time = it }
        entity.progress?.let { challenge.progress = it }
        entity.difficulty?.let { challenge.difficulty = it }
        entity.experience?.let { challenge.experience = it }
        entity.finishedDistance?.let { challenge.finishedDistance = it }
        entity.finishedTime?.let { challenge.finishedTime = it }

        return challenge
    }

    fun transformToEntity(challenge: Challenge) : ChallengeEntity {
        val entity = ChallengeEntity()
        entity.id = challenge.id
        entity.isFinished = challenge.isFinished
        entity.deadline = challenge.deadline
        entity.title = challenge.title
        entity.distance = challenge.distance
        entity.speed = challenge.speed
        entity.time = challenge.time
        entity.progress = challenge.progress
        entity.difficulty = challenge.difficulty
        entity.experience = challenge.experience
        entity.finishedDistance = challenge.finishedDistance
        entity.finishedTime = challenge.finishedTime

        return entity
    }
}