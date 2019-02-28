package com.whisker.mrr.firebase.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_CHALLENGES
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.repository.ChallengeRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import kotlin.collections.HashMap

class ChallengeDataRepository(private val databaseReference: DatabaseReference) : ChallengeRepository {

    companion object {
        const val DB_REFERENCE_IS_FINISHED = "finished"
        const val DB_REFERENCE_PROGRESS = "progress"
        const val DB_REFERENCE_FINISHED_DISTANCE = "finishedDistance"
        const val DB_REFERENCE_FINISHED_TIME = "finishedTime"
    }

    override fun saveChallenge(userId: String, challenge: Challenge): Completable {
        val reference = getReference(userId)
        challenge.id = reference.push().key!!

        return Completable.create { emitter ->
            reference.child(challenge.id).setValue(challenge).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    task.exception?.let {
                        emitter.onError(it)
                    }
                }
            }
        }
    }

    override fun updateChallenges(userId: String, challenges: List<Challenge>): Completable {
        val reference = getReference(userId)
        val completableList = mutableListOf<Completable>()
        for(challenge in challenges) {
            completableList.add(
                Completable.create { emitter ->
                    val map = HashMap<String, Any>()
                    map[DB_REFERENCE_PROGRESS] = challenge.progress
                    map[DB_REFERENCE_IS_FINISHED] = challenge.isFinished
                    map[DB_REFERENCE_FINISHED_DISTANCE] = challenge.finishedDistance
                    map[DB_REFERENCE_FINISHED_TIME] = challenge.finishedTime
                    reference.child(challenge.id).updateChildren(map).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            task.exception?.let {
                                emitter.onError(it)
                            }
                        }
                    }
                }
            )
        }

        return Completable.concat(completableList)
    }

    override fun getChallenges(userId: String): Flowable<List<Challenge>> {
        return Flowable.create( { emitter ->
            getReference(userId).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val challenges = mutableListOf<Challenge>()
                    dataSnapshot.children.forEach { child ->
                        child.getValue(Challenge::class.java)?.let {
                            challenges.add(it)
                        }
                    }
                    emitter.onNext(challenges.reversed())
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }, BackpressureStrategy.LATEST)
    }

    override fun getActiveChallenges(userId: String) : Flowable<List<Challenge>> {
        return Flowable.create( { emitter ->
            getReference(userId).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val activeChallenges = dataSnapshotToActiveChallenges(dataSnapshot)
                    emitter.onNext(activeChallenges)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }, BackpressureStrategy.LATEST)
    }

    override fun getActiveChallengesSingle(userId: String): Single<List<Challenge>> {
        return Single.create { emitter ->
            getReference(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val activeChallenges = dataSnapshotToActiveChallenges(dataSnapshot)
                    emitter.onSuccess(activeChallenges)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }
    }

    private fun dataSnapshotToActiveChallenges(dataSnapshot: DataSnapshot) : List<Challenge> {
        val currentTime = Date().time
        val activeChallenges = mutableListOf<Challenge>()
        dataSnapshot.children.forEach { child ->
            child.getValue(Challenge::class.java)?.let { challenge ->
                challenge.deadline?.let {
                    if(it > currentTime && !challenge.isFinished)
                        activeChallenges.add(challenge)
                } ?: challenge.run {
                    if(!this.isFinished)
                        activeChallenges.add(this)
                }
            }
        }
        return activeChallenges
    }

    private fun getReference(userId: String) : DatabaseReference {
        return databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_CHALLENGES)
    }
}