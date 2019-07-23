package com.whisker.mrr.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.common.bus.event.NetworkStateEvent
import com.whisker.mrr.firebase.common.DataConstants.DB_AVERAGE_PACE
import com.whisker.mrr.firebase.common.DataConstants.DB_EXPERIENCE
import com.whisker.mrr.firebase.common.DataConstants.DB_TOTAL_DISTANCE
import com.whisker.mrr.firebase.common.DataConstants.DB_TOTAL_TIME
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USER_STATS
import com.whisker.mrr.domain.model.UserStats
import com.whisker.mrr.data.source.RemoteUserSource
import com.whisker.mrr.domain.common.utils.UserStatsUtils
import com.whisker.mrr.domain.model.UserStatsProgress
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RemoteUserDataSource
@Inject constructor(
    private val databaseReference: DatabaseReference,
    firebaseAuth: FirebaseAuth
) : BaseSource(firebaseAuth), RemoteUserSource {

    init {
        RxBus.subscribeSticky(NetworkStateEvent::class.java.name, this, Consumer { event ->
            if(event is NetworkStateEvent) {
                isNetworkAvailable = event.isNetworkAvailable
            }
        })
    }

    override fun updateUserStats(userStats: UserStats): Completable {
        return checkConnection().andThen(
                Completable.create { emitter ->
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
        )
    }

    override fun getUserStats(): Single<UserStats> {
        return checkConnection().andThen(
            Single.create<UserStats> { emitter ->
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
        )
    }

    override fun createUserStats(userStats: UserStats): Completable {
        return checkConnection().andThen(
            Completable.create { emitter ->
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
            }
        )
        .observeOn(Schedulers.io())
    }

    override fun updateUserStats(progressList: List<UserStatsProgress>): Completable {
        return checkConnection().andThen(
            getUserStats().flatMapCompletable { userStats ->
                Completable.create { emitter ->
                    var userReference: DatabaseReference = databaseReference
                    try {
                        userReference = getReference()
                    } catch (e : FirebaseAuthInvalidUserException) {
                        emitter.onError(e)
                    }

                    val map = HashMap<String, Any>()
                    val distance = userStats.totalDistance + progressList.map { it.distanceProgress }.sum()
                    val time = userStats.totalTime + progressList.map { it.timeProgress }.sum()
                    val exp = userStats.experience + progressList.map { it.expProgress }.sum()
                    map[DB_TOTAL_DISTANCE] = distance
                    map[DB_TOTAL_TIME] = time
                    map[DB_EXPERIENCE] = exp
                    map[DB_AVERAGE_PACE] = UserStatsUtils.calculateAveragePace(distance, time)

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

        )
    }

    //TODO: AuthException in domain
    @Throws(FirebaseAuthInvalidUserException::class)
    private fun getReference() : DatabaseReference {
        return databaseReference
            .child(REFERENCE_USERS)
            .child(getUserID())
            .child(REFERENCE_USER_STATS)
    }
}