package com.whisker.mrr.xrunner.data.repository

import com.whisker.mrr.xrunner.domain.model.RouteStatsEntity
import com.whisker.mrr.xrunner.domain.model.UserStatsEntity
import com.whisker.mrr.xrunner.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single

class UserDataRepository : UserRepository {

    override fun updateUserStats(userId: String, route: RouteStatsEntity): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserStats(userId: String): Single<UserStatsEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUserStats(userId: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}