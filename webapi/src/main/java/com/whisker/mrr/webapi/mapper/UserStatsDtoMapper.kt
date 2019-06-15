package com.whisker.mrr.webapi.mapper

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.webapi.dto.UserStatsDto

object UserStatsDtoMapper {

    fun transformFromDto(statsDto: UserStatsDto) : UserStats {
        val stats = UserStats(statsDto.userID)
        statsDto.averagePace?.let { stats.averagePace = it }
        statsDto.experience?.let { stats.experience = it }
        statsDto.totalDistance?.let { stats.totalDistance = it }
        statsDto.totalTime?.let { stats.totalTime = it }

        return stats
    }

    fun transformToDto(stats: UserStats) : UserStatsDto {
        val statsDto = UserStatsDto(stats.userID)
        statsDto.averagePace = stats.averagePace
        statsDto.experience = stats.experience
        statsDto.totalDistance = stats.totalDistance
        statsDto.totalTime = stats.totalTime

        return statsDto
    }
}