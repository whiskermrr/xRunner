package com.whisker.mrr.room.source

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.data.source.LocalChallengeSource
import com.whisker.mrr.domain.model.ChallengeProgress
import com.whisker.mrr.room.dao.ChallengeDao
import com.whisker.mrr.room.dao.ChallengeProgressDao
import com.whisker.mrr.room.mapper.ChallengeEntityMapper
import com.whisker.mrr.room.mapper.ChallengeProgressEntityMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalChallengeDataSource(
    private val challengeDao: ChallengeDao,
    private val challengeProgressDao: ChallengeProgressDao
) : LocalChallengeSource {

    override fun saveChallenge(challenge: Challenge): Single<Long> {
        return Single.fromCallable {
            val nextID = challengeDao.getNextLocalID()
            nextID?.let { challenge.id = nextID } ?: run { challenge.id   = -1L }
            challengeDao.insert(ChallengeEntityMapper.transformToEntity(challenge))
        }
     }

    override fun saveChallenges(challenges: List<Challenge>): Completable {
        return Completable.fromAction {
            if(challenges.isNotEmpty()) {
                challengeDao.insertAll(ChallengeEntityMapper.transformListToEntities(challenges))
                challengeDao.deleteIsDeleted()
            }
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

    override fun updateChallengeID(oldID: Long, newID: Long): Completable {
        return Completable.fromAction {
            if(oldID != newID) {
                challengeDao.updateChallengeID(oldID, newID)
            }
        }
    }

    override fun saveChallengesProgressListLocally(progressList: List<ChallengeProgress>): Completable {
        return Completable.fromAction {
            challengeProgressDao.insertAll(ChallengeProgressEntityMapper.transofrmListToEntities(progressList))
        }
    }

    override fun getChallengesProgressList(): Single<List<ChallengeProgress>> {
        return challengeProgressDao.getChallengeProgressList()
            .map { ChallengeProgressEntityMapper.transformListFromEntities(it) }
            .onErrorReturn { listOf() }
    }

    override fun removeChallengesProgressList(): Completable {
        return Completable.fromAction {
            challengeProgressDao.clearChallengeProgressTable()
        }
    }

    override fun removeChallengesSavedLocally(): Completable {
        return Completable.fromAction {
            challengeDao.removeChallengesSavedLocally()
        }
    }

    override fun getChallengesSavedLocallyAndDeleted(): Single<List<Challenge>> {
        return challengeDao.getChallengesSavedLocallyAndDeleted()
            .map { ChallengeEntityMapper.transformListFromEntities(it) }
    }
}