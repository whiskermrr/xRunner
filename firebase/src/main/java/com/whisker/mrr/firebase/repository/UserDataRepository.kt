package com.whisker.mrr.firebase.repository

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
import com.whisker.mrr.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserDataRepository
@Inject constructor(private val databaseReference: DatabaseReference)
: UserRepository {

    override fun updateUserStats(userId: String, userStats: UserStats): Completable {
        return Completable.create { emitter ->
            val map = HashMap<String, Any>()
            map[DB_TOTAL_DISTANCE] = userStats.totalDistance
            map[DB_TOTAL_TIME] = userStats.totalTime
            map[DB_EXPERIENCE] = userStats.experience
            map[DB_AVERAGE_PACE] = userStats.averagePace

            getReference(userId).updateChildren(map).addOnCompleteListener { task ->
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
        }
    }

    override fun getUserStats(userId: String): Single<UserStats> {
        return Single.create { emitter ->
            getReference(userId).addListenerForSingleValueEvent(object: ValueEventListener {
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
        }
    }

    override fun createUserStats(userId: String): Completable {
        return Completable.create { emitter ->
            getReference(userId).setValue(UserStats()).addOnCompleteListener { task ->
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
        }
    }

    private fun getReference(userId: String) : DatabaseReference {
        return databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_USER_STATS)
    }
}