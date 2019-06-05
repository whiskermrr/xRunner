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
            .flatMapCompletable { localID ->
                challenge.id = localID
                remoteChallengeSource.saveChallenge(challenge)
                    .flatMapCompletable { localChallengeSource.updateChallengeID(localID, it) }
            }.onErrorComplete() //TODO: custom NoInternetException
    }

    override fun updateChallenges(challenges: List<Challenge>): Completable {
        return Completable.concatArray(
            localChallengeSource.updateChallenges(challenges),
            remoteChallengeSource.updateChallenges(challenges)
        )
    }

    override fun getChallenges(): Flowable<List<Challenge>> {
        val localFlowable = localChallengeSource.getChallenges()

        val remoteFlowable = remoteChallengeSource.getChallenges()
            .flatMapCompletable { challenges ->
                localChallengeSource.saveChallenges(challenges)
            }.andThen(Flowable.empty<List<Challenge>>())

        return localFlowable.mergeWith(remoteFlowable)
    }

    override fun getActiveChallenges(): Flowable<List<Challenge>> {
        val localFlowable = localChallengeSource.getActiveChallenges()

        val remoteFlowable = remoteChallengeSource.getChallenges()
            .flatMapCompletable { challenges ->
                localChallengeSource.saveChallenges(challenges)
            }.andThen(Flowable.empty<List<Challenge>>())

        return localFlowable.mergeWith(remoteFlowable)
    }

    override fun getActiveChallengesSingle(): Single<List<Challenge>> {
        return localChallengeSource.getActiveChallengesSingle()
    }
}