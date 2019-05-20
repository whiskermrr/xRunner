package com.whisker.mrr.data.source

import com.whisker.mrr.data.database.dao.ChallengeDao
import com.whisker.mrr.data.mapper.ChallengeEntityMapper
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.source.LocalChallengeSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalChallengeDataSource(
    private val challengeDao: ChallengeDao
) : LocalChallengeSource {

    override fun saveChallenge(challenge: Challenge): Single<Long> {
        return Single.fromCallable {
            val nextID = challengeDao.getNextLocalID()
            nextID?.let { challenge.id = nextID } ?: kotlin.run { challenge.id   = -1L }
            challengeDao.insert(ChallengeEntityMapper.transformToEntity(challenge))
        }
     }

    override fun saveChallenges(challenges: List<Challenge>): Completable {
        return Completable.fromAction {
            challengeDao.insertAll(ChallengeEntityMapper.transformListToEntities(challenges))
            challengeDao.deleteIsDeleted()
        }
    }

    override fun getChallenges(): Flowable<List<Challenge>> {
        return challengeDao.getChallenges().map { ChallengeEntityMapper.transformListFromEntities(it) }
    }

    override fun getActiveChallenges(): Flowable<List<Challenge>> {
        return challengeDao.getActiveChallenges().map { ChallengeEntityMapper.transformListFromEntities(it) }
    }

    override fun getActiveChallengesSingle(): Single<List<Challenge>> {
        return challengeDao.getActiveChallengesSingle().map { ChallengeEntityMapper.transformListFromEntities(it) }
    }

    override fun removeChallengeById(challengeID: Long): Completable {
        return Completable.fromAction {
            challengeDao.deleteChallengeById(challengeID)
        }
    }

    override fun markChallengeAsDeleted(challengeID: Long): Completable {
        return Completable.fromAction {
            challengeDao.markChallengeAsDeleted(challengeID)
        }
    }

    override fun updateChallenges(challenges: List<Challenge>): Completable {
        return Completable.fromAction {
            challengeDao.updateChallenges(ChallengeEntityMapper.transformListToEntities(challenges))
        }
    }

    override fun updateChallengeID(newID: Long, oldID: Long): Completable {
        return Completable.fromAction {
            challengeDao.updateChallengeID(newID, oldID)
        }
    }
}