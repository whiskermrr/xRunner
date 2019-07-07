package com.whisker.mrr.data.repository

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.data.source.LocalUserSource
import com.whisker.mrr.data.source.RemoteUserSource
import com.whisker.mrr.domain.model.UserStatsProgress
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class UserDataRepository(
    private val localUserSource: LocalUserSource,
    private val remoteUserSource: RemoteUserSource
) : UserRepository {

    override fun updateUserStats(userStats: UserStats): Completable {
        return Completable.concatArray(
            localUserSource.updateUserStats(userStats),
            remoteUserSource.updateUserStats(userStats))
    }

    override fun getUserStats(): Flowable<UserStats> {
        val localFlowable = localUserSource.getUserStats()
        val remoteFlowable = remoteUserSource.getUserStats()
            .flatMapCompletable {  stats ->
                localUserSource.saveUserStats(stats)
            }.andThen(Flowable.empty<UserStats>())

        return localFlowable.mergeWith(remoteFlowable)
    }

    override fun createUserStats(userID: String): Completable {
        return Single.just(UserStats(userID))
            .flatMapCompletable {
                localUserSource.createUserStats(it)
                .andThen(remoteUserSource.createUserStats(it))
            }
    }

    override fun synchronizeUserStats(): Completable {
        return remoteUserSource.getUserStats()
            .flatMapCompletable { localUserSource.saveUserStats(it) }
    }

    override fun saveUserStatsProgressLocally(statsProgress: UserStatsProgress) : Completable {
        return localUserSource.saveUserStatsProgressLocally(statsProgress)
    }
}