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
import java.util.*
import kotlin.collections.HashMap

class ChallengeDataRepository(private val databaseReference: DatabaseReference) : ChallengeRepository {

    companion object {
        const val DB_REFERENCE_IS_FINISHED = "isFinished"
        const val DB_REFERENCE_PROGRESS = "progress"
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
        for(achievement in challenges) {
            completableList.add(
                Completable.create { emitter ->
                    val map = HashMap<String, Any>()
                    map[DB_REFERENCE_PROGRESS] = achievement.progress
                    map[DB_REFERENCE_IS_FINISHED] = achievement.isFinished
                    reference.child(achievement.id).updateChildren(map).addOnCompleteListener { task ->
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
                    emitter.onNext(challenges)
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
                    val currentTime = Date().time
                    val activeAchievements = mutableListOf<Challenge>()
                    dataSnapshot.children.forEach { child ->
                        child.getValue(Challenge::class.java)?.let { achievement ->
                            achievement.deadline?.let {
                                if(it > currentTime && !achievement.isFinished)
                                    activeAchievements.add(achievement)
                            } ?: achievement.run {
                                if(!this.isFinished)
                                    activeAchievements.add(this)
                            }
                        }
                    }
                    emitter.onNext(activeAchievements)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }, BackpressureStrategy.LATEST)
    }

    private fun getReference(userId: String) : DatabaseReference {
        return databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_CHALLENGES)
    }
}