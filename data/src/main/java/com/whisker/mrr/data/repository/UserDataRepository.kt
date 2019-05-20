package com.whisker.mrr.data.repository

import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.source.LocalUserSource
import com.whisker.mrr.domain.source.RemoteUserSource
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
            .flatMapPublisher {  stats ->
                Completable.fromAction {
                    localUserSource.updateUserStats(stats)
                }.andThen(Flowable.empty<UserStats>())
            }

        return Flowable.concatArray(localFlowable, remoteFlowable)
    }

    override fun createUserStats(userID: String): Completable {
        return Single.just(UserStats(userID))
            .flatMapCompletable {
                localUserSource.createUserStats(it)
                .andThen(remoteUserSource.createUserStats(it))
            }.doOnError { it.printStackTrace() }
    }
}