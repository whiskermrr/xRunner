package com.whisker.mrr.data.source

import com.whisker.mrr.data.database.dao.UserStatsDao
import com.whisker.mrr.data.mapper.UserStatsEntityMapper
import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.source.LocalUserSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalUserDataSource(
    private val userStatsDao : UserStatsDao
) : LocalUserSource {

    override fun updateUserStats(userStats: UserStats): Completable {
        return Completable.fromAction {
            userStatsDao.updateUserStats(UserStatsEntityMapper.transformToEntity(userStats))
        }
    }

    override fun getUserStats(): Flowable<UserStats> {
        return userStatsDao.getUserStats().map { UserStatsEntityMapper.transformFromEntity(it) }
    }

    override fun createUserStats(userStats: UserStats): Completable {
        return Completable.fromAction {
            userStatsDao.insert(UserStatsEntityMapper.transformToEntity(userStats))
        }
    }

    override fun getUserStatsSingle(): Single<UserStats> {
        return userStatsDao.getUserStatsSingle().map { UserStatsEntityMapper.transformFromEntity(it) }
    }

    override fun saveUserStats(userStats: UserStats): Completable {
        return Completable.fromAction {
            userStatsDao.insert(UserStatsEntityMapper.transformToEntity(userStats))
        }
    }
}