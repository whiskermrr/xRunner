package com.whisker.mrr.webapi.mapper

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.webapi.dto.ChallengeDto

object ChallengeDtoMapper {

    fun transformListFromDtoList(challengeDtoList: List<ChallengeDto>) : List<Challenge> {
        val challenges = mutableListOf<Challenge>()
        for(entity in challengeDtoList) {
            challenges.add(transformFromDto(entity))
        }
        return challenges
    }

    fun transformListToDtoList(challenges: List<Challenge>) : List<ChallengeDto> {
        val challengeDtoList = mutableListOf<ChallengeDto>()
        for(challenge in challenges) {
            challengeDtoList.add(transformToDto(challenge))
        }
        return challengeDtoList
    }

    fun transformFromDto(challengeDto: ChallengeDto) : Challenge {
        val challenge = Challenge()
        challengeDto.id?.let { challenge.id = it }
        challengeDto.isFinished?.let { challenge.isFinished = it }
        challengeDto.deadline?.let { challenge.deadline = it }
        challengeDto.title?.let { challenge.title = it }
        challengeDto.distance?.let { challenge.distance = it }
        challengeDto.speed?.let { challenge.speed = it }
        challengeDto.time?.let { challenge.time = it }
        challengeDto.progress?.let { challenge.progress = it }
        challengeDto.difficulty?.let { challenge.difficulty = it }
        challengeDto.experience?.let { challenge.experience = it }
        challengeDto.finishedDistance?.let { challenge.finishedDistance = it }
        challengeDto.finishedTime?.let { challenge.finishedTime = it }

        return challenge
    }

    fun transformToDto(challenge: Challenge) : ChallengeDto {
        val challengeDto = ChallengeDto()
        challengeDto.id = challenge.id
        challengeDto.isFinished = challenge.isFinished
        challengeDto.deadline = challenge.deadline
        challengeDto.title = challenge.title
        challengeDto.distance = challenge.distance
        challengeDto.speed = challenge.speed
        challengeDto.time = challenge.time
        challengeDto.progress = challenge.progress
        challengeDto.difficulty = challenge.difficulty
        challengeDto.experience = challenge.experience
        challengeDto.finishedDistance = challenge.finishedDistance
        challengeDto.finishedTime = challenge.finishedTime

        return challengeDto
    }
}