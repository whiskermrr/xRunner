package com.whisker.mrr.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.*
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_CHALLENGES
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.source.RemoteChallengeSource
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class RemoteChallengeDataSource(
    private val databaseReference: DatabaseReference,
    connectivityReference: DatabaseReference,
    firebaseAuth: FirebaseAuth
) : BaseSource(connectivityReference, firebaseAuth), RemoteChallengeSource {

    companion object {
        const val DB_REFERENCE_IS_FINISHED = "finished"
        const val DB_REFERENCE_PROGRESS = "progress"
        const val DB_REFERENCE_FINISHED_DISTANCE = "finishedDistance"
        const val DB_REFERENCE_FINISHED_TIME = "finishedTime"
    }

    override fun saveChallenge(challenge: Challenge): Single<Long> {
        return checkConnection().andThen(
            Single.create<Long> { emitter ->
                var reference: DatabaseReference = databaseReference
                try {
                    reference = getReference()
                } catch (e: Exception) {
                    emitter.onError(e)
                }
                challenge.id = System.currentTimeMillis()

                reference.child(challenge.id.toString()).setValue(challenge).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        emitter.onSuccess(challenge.id)
                    } else {
                        task.exception?.let {
                            emitter.onError(it)
                        }
                    }
                }
            }.observeOn(Schedulers.io())
        )
    }

    override fun updateChallenges(challenges: List<Challenge>): Completable {
        val reference = getReference()
        val completableList = mutableListOf<Completable>()
        for(challenge in challenges) {
            completableList.add(
                Completable.create { emitter ->
                    val map = HashMap<String, Any>()
                    map[DB_REFERENCE_PROGRESS] = challenge.progress
                    map[DB_REFERENCE_IS_FINISHED] = challenge.isFinished
                    map[DB_REFERENCE_FINISHED_DISTANCE] = challenge.finishedDistance
                    map[DB_REFERENCE_FINISHED_TIME] = challenge.finishedTime
                    reference.child(challenge.id.toString()).updateChildren(map).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            task.exception?.let {
                                emitter.onError(it)
                            }
                        }
                    }
                }.observeOn(Schedulers.io())
            )
        }
        return checkConnection().andThen(Completable.concat(completableList))
    }

    override fun getChallenges(): Single<List<Challenge>> {
        return Single.create<List<Challenge>> { emitter ->
            var reference: DatabaseReference = databaseReference
            try {
                reference = getReference()
            } catch (e: Exception) {
                emitter.onError(e)
            }
            reference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val challenges = mutableListOf<Challenge>()
                    dataSnapshot.children.forEach { child ->
                        child.getValue(Challenge::class.java)?.let {
                            challenges.add(it)
                        }
                    }
                    emitter.onSuccess(challenges.reversed())
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }.observeOn(Schedulers.io()).onErrorReturn { emptyList() }
    }

    override fun saveChallenges(challenges: List<Challenge>): Single<List<Long>> {
        val singles = mutableListOf<Single<Long>>()
        for(challenge in challenges) {
            singles.add(saveChallenge(challenge))
        }
        return checkConnection().andThen(Single.zip(singles) { args -> Arrays.asList(args) as List<Long> })
    }

    override fun removeChallengeById(challengeID: Long): Completable {
        return checkConnection().andThen {
            Completable.create { emitter ->
                var reference: DatabaseReference = databaseReference
                try {
                    reference = getReference().child(challengeID.toString())
                } catch (e: Exception) {
                    emitter.onError(e)
                }

                reference.removeValue().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        task.exception?.let {
                            emitter.onError(it)
                        }
                    }
                }.addOnFailureListener {
                    emitter.onError(it)
                }
            }.observeOn(Schedulers.io())
        }
    }

    @Throws(FirebaseAuthInvalidUserException::class)
    private fun getReference() : DatabaseReference {
        return databaseReference
            .child(REFERENCE_USERS)
            .child(getUserID())
            .child(REFERENCE_CHALLENGES)
    }
}