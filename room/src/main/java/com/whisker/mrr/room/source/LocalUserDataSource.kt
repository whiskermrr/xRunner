package com.whisker.mrr.room.source

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.data.source.LocalUserSource
import com.whisker.mrr.domain.model.UserStatsProgress
import com.whisker.mrr.room.dao.UserStatsDao
import com.whisker.mrr.room.dao.UserStatsProgressDao
import com.whisker.mrr.room.mapper.UserStatsEntityMapper
import com.whisker.mrr.room.mapper.UserStatsProgressMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalUserDataSource(
    private val userStatsDao : UserStatsDao,
    private val userStatsProgressDao: UserStatsProgressDao
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

    override fun saveUserStatsProgressLocally(statsProgress: UserStatsProgress): Completable {
        return Completable.fromAction {
            userStatsProgressDao.insert(UserStatsProgressMapper.trasnformToEntity(statsProgress))
        }
    }
}