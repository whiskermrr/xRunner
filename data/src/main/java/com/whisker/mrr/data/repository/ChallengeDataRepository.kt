package com.whisker.mrr.data.repository

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.data.source.LocalChallengeSource
import com.whisker.mrr.data.source.RemoteChallengeSource
import com.whisker.mrr.domain.model.ChallengeProgress
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class ChallengeDataRepository(
    private val localChallengeSource: LocalChallengeSource,
    private val remoteChallengeSource: RemoteChallengeSource
) : ChallengeRepository {

    override fun saveChallenge(challenge: Challenge): Completable {
        return localChallengeSource.saveChallenge(challenge)
            .flatMapCompletable { synchronizeChallenges() }
    }

    override fun updateChallenges(challenges: List<Challenge>): Completable {
        return Completable.concatArray(
            localChallengeSource.updateChallenges(challenges),
            remoteChallengeSource.updateChallenges(challenges)
        )
    }

    override fun getChallenges(): Flowable<List<Challenge>> {
        val localFlowable = localChallengeSource.getChallenges()

        val remoteFlowable = synchronizeChallenges()
            .andThen(Flowable.empty<List<Challenge>>())

        return localFlowable.mergeWith(remoteFlowable)
    }

    override fun getActiveChallenges(): Flowable<List<Challenge>> {
        val localFlowable = localChallengeSource.getActiveChallenges()

        val remoteFlowable = synchronizeChallenges()
            .andThen(Flowable.empty<List<Challenge>>())

        return localFlowable.mergeWith(remoteFlowable)
    }

    override fun getActiveChallengesSingle(): Single<List<Challenge>> {
        return localChallengeSource.getActiveChallengesSingle()
    }

    override fun saveChallengesProgressListLocally(progressList: List<ChallengeProgress>): Completable {
        return localChallengeSource.saveChallengesProgressListLocally(progressList)
    }

    override fun synchronizeChallenges(): Completable {
        return localChallengeSource.getChallengesSavedLocallyAndDeleted()
            .flatMap { remoteChallengeSource.saveChallenges(it) }
            .flatMapCompletable { localChallengeSource.removeChallengesSavedLocally() }
            .andThen(localChallengeSource.getChallengesProgressList())
            .flatMapCompletable { remoteChallengeSource.updateChallengesProgress(it) }
            .andThen(localChallengeSource.removeChallengesProgressList())
            .andThen(remoteChallengeSource.getChallenges().flatMapCompletable { localChallengeSource.saveChallenges(it) })
            .onErrorComplete()
    }
}