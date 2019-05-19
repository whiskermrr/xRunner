package com.whisker.mrr.data.repository

import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import com.whisker.mrr.domain.source.LocalChallengeSource
import com.whisker.mrr.domain.source.RemoteChallengeSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class ChallengeDataRepository(
    private val localChallengeSource: LocalChallengeSource,
    private val remoteChallengeSource: RemoteChallengeSource
) : ChallengeRepository {

    override fun saveChallenge(challenge: Challenge): Completable {
        return localChallengeSource.saveChallenge(challenge)
            .flatMap {  localID ->
                challenge.id = localID
                remoteChallengeSource.saveChallenge(challenge)
            }
            .flatMapCompletable { localChallengeSource.updateChallengeID(challenge.id, it) }
    }

    override fun updateChallenges(challenges: List<Challenge>): Completable {
        return localChallengeSource.updateChallenges(challenges)
            .andThen { remoteChallengeSource.updateChallenges(challenges) }
    }

    override fun getChallenges(): Flowable<List<Challenge>> {
        val localFlowable = localChallengeSource.getChallenges()

        val remoteFlowable = remoteChallengeSource.getChallenges()
            .flatMapPublisher { challenges ->
                Completable.fromAction {
                    localChallengeSource.saveChallenges(challenges)
                }.andThen(Flowable.empty<List<Challenge>>())
            }

        return Flowable.concatArray(localFlowable, remoteFlowable)
    }

    override fun getActiveChallenges(): Flowable<List<Challenge>> {
        val localFlowable = localChallengeSource.getActiveChallenges()

        val remoteFlowable = remoteChallengeSource.getChallenges()
            .flatMapPublisher { challenges ->
                Completable.fromAction {
                    localChallengeSource.saveChallenges(challenges)
                }.andThen(Flowable.empty<List<Challenge>>())
            }

        return Flowable.concatArray(localFlowable, remoteFlowable)
    }

    override fun getActiveChallengesSingle(): Single<List<Challenge>> {
        return localChallengeSource.getActiveChallengesSingle()
    }
}