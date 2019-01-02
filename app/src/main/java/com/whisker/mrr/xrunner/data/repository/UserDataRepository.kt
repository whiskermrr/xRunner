package com.whisker.mrr.xrunner.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.whisker.mrr.xrunner.domain.model.RouteStatsEntity
import com.whisker.mrr.xrunner.domain.model.UserStatsEntity
import com.whisker.mrr.xrunner.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import kotlin.math.roundToInt

class UserDataRepository
@Inject constructor(private val databaseReference: DatabaseReference)
: UserRepository {

    companion object {
        const val REFERENCE_USERS = "Users"
        const val REFERENCE_USER_STATS = "UserStats"
        const val DB_AVERAGE_PACE = "averagePace"
        const val DB_EXPERIENCE = "experience"
        const val DB_TOTAL_DISTANCE = "totalDistance"
        const val DB_TOTAL_TIME = "totalTime"
    }

    override fun updateUserStats(userId: String, stats: RouteStatsEntity): Completable {
        val reference = databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_USER_STATS)

        return getUserStats(userId)
            .flatMapCompletable { userStats ->
                val map = HashMap<String, Any>()
                map[DB_TOTAL_DISTANCE] = userStats.totalDistance + stats.wgs84distance
                map[DB_TOTAL_TIME] = userStats.totalTime + stats.routeTime
                map[DB_EXPERIENCE] = userStats.experience + calculateExp(stats)

                Completable.create { emitter ->
                    reference.updateChildren(map).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            emitter.onComplete()
                        } else if(task.exception != null) {
                            emitter.onError(task.exception!!)
                        }
                    }.addOnFailureListener {
                        emitter.onError(it)
                    }
                }
            }
    }

    override fun getUserStats(userId: String): Single<UserStatsEntity> {
        val reference = databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_USER_STATS)

        return Single.create { emitter ->
            reference.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userStats = dataSnapshot.getValue(UserStatsEntity::class.java)
                    emitter.onSuccess(userStats!!)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }
    }

    override fun createUserStats(userId: String): Completable {
        val reference = databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_USER_STATS)

        return Completable.create { emitter ->
            reference.setValue(UserStatsEntity()).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    emitter.onComplete()
                } else if(task.exception != null) {
                    emitter.onError(task.exception!!)
                }
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

    private fun calculateExp(stats: RouteStatsEntity) : Int {
        return ((stats.averageSpeed * stats.wgs84distance) / 10f).roundToInt()
    }
}