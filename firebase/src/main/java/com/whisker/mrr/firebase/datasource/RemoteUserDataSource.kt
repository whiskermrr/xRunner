package com.whisker.mrr.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.whisker.mrr.firebase.common.DataConstants.DB_AVERAGE_PACE
import com.whisker.mrr.firebase.common.DataConstants.DB_EXPERIENCE
import com.whisker.mrr.firebase.common.DataConstants.DB_TOTAL_DISTANCE
import com.whisker.mrr.firebase.common.DataConstants.DB_TOTAL_TIME
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USER_STATS
import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.domain.source.RemoteUserSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RemoteUserDataSource
@Inject constructor(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : RemoteUserSource {

    override fun updateUserStats(userStats: UserStats): Completable {
        return Completable.create { emitter ->
            var userReference: DatabaseReference = databaseReference
            try {
                userReference = getReference()
            } catch (e : FirebaseAuthInvalidUserException) {
                emitter.onError(e)
            }

            val map = HashMap<String, Any>()
            map[DB_TOTAL_DISTANCE] = userStats.totalDistance
            map[DB_TOTAL_TIME] = userStats.totalTime
            map[DB_EXPERIENCE] = userStats.experience
            map[DB_AVERAGE_PACE] = userStats.averagePace

            userReference.updateChildren(map).addOnCompleteListener { task ->
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

    override fun getUserStats(): Single<UserStats> {
        return Single.create<UserStats> { emitter ->
            var userReference: DatabaseReference = databaseReference
            try {
                userReference = getReference()
            } catch (e : FirebaseAuthInvalidUserException) {
                emitter.onError(e)
            }
            userReference.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.value?.let {
                        dataSnapshot.getValue(UserStats::class.java)?.let { userStats ->
                            emitter.onSuccess(userStats)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }.observeOn(Schedulers.io())
    }

    override fun createUserStats(userStats: UserStats): Completable {
        return Completable.create { emitter ->
            var userReference: DatabaseReference = databaseReference
            try {
                userReference = getReference()
            } catch (e : FirebaseAuthInvalidUserException) {
                emitter.onError(e)
            }
            userReference.setValue(userStats).addOnCompleteListener { task ->
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

    @Throws(FirebaseAuthInvalidUserException::class)
    private fun getReference() : DatabaseReference {
        var userUID = ""
        firebaseAuth.currentUser?.uid?.let { userUID = it } ?: run { throw FirebaseAuthInvalidUserException("", "") }
        return databaseReference
            .child(REFERENCE_USERS)
            .child(userUID)
            .child(REFERENCE_USER_STATS)
    }
}