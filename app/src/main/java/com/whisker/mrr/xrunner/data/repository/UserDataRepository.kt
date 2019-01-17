package com.whisker.mrr.xrunner.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.whisker.mrr.xrunner.data.common.DataConstants.DB_AVERAGE_PACE
import com.whisker.mrr.xrunner.data.common.DataConstants.DB_EXPERIENCE
import com.whisker.mrr.xrunner.data.common.DataConstants.DB_TOTAL_DISTANCE
import com.whisker.mrr.xrunner.data.common.DataConstants.DB_TOTAL_TIME
import com.whisker.mrr.xrunner.data.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.xrunner.data.common.DataConstants.REFERENCE_USER_STATS
import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_SECOND
import com.whisker.mrr.domain.common.DomainConstants.MINUTES_PER_HOUR
import com.whisker.mrr.domain.model.RouteStatsEntity
import com.whisker.mrr.domain.model.UserStatsEntity
import com.whisker.mrr.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import kotlin.math.roundToInt

class UserDataRepository
@Inject constructor(private val databaseReference: DatabaseReference)
: UserRepository {

    override fun updateUserStats(userId: String, stats: RouteStatsEntity): Completable {

        return getUserStats(userId)
            .flatMapCompletable { userStats ->
                val map = HashMap<String, Any>()
                val totalDistance = userStats.totalDistance + stats.wgs84distance
                val totalTime = userStats.totalTime + stats.routeTime
                map[DB_TOTAL_DISTANCE] = totalDistance
                map[DB_TOTAL_TIME] = totalTime
                map[DB_EXPERIENCE] = userStats.experience + calculateExp(stats)
                map[DB_AVERAGE_PACE] = calculateAveragePace(totalDistance, totalTime)

                Completable.create { emitter ->
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
    }

    override fun getUserStats(userId: String): Single<UserStatsEntity> {
        return Single.create { emitter ->
            getReference(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.value?.let {
                        dataSnapshot.getValue(UserStatsEntity::class.java)?.let { userStats ->
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
            getReference(userId).setValue(UserStatsEntity()).addOnCompleteListener { task ->
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

    private fun calculateAveragePace(totalDistanceInMeters: Float, time: Long) : Float {
        val totalTimeInSeconds = (time / MILLISECONDS_PER_SECOND).toInt()
        return MINUTES_PER_HOUR / ((totalDistanceInMeters / totalTimeInSeconds) * 3.6f)
    }

    private fun calculateExp(stats: RouteStatsEntity) : Int {
        return ((stats.averageSpeed * stats.wgs84distance) / 10f).roundToInt()
    }

    private fun getReference(userId: String) : DatabaseReference {
        return databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_USER_STATS)
    }
}