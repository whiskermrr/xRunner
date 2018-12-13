package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.database.*
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.utils.DateUtils
import com.whisker.mrr.xrunner.utils.xRunnerConstants.REFERENCE_ROUTES
import com.whisker.mrr.xrunner.utils.xRunnerConstants.REFERENCE_USERS
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject
import kotlin.collections.HashMap

class RouteDatabaseSource @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {

    fun saveRoute(route: Route, userId : String) : Completable {
        val databaseReference = firebaseDatabase.reference
            .child(REFERENCE_USERS).child(userId)
            .child(REFERENCE_ROUTES)
            .child(DateUtils.getFirstDayOfTheMonthInMillis(route.date).toString())

        route.routeId = databaseReference.push().key!!
        return Completable.create { emitter ->
            databaseReference.child(route.routeId).setValue(route).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    emitter.onComplete()
                } else if(task.exception != null) {
                    emitter.onError(task.exception!!)
                }
            }
        }
    }

    fun getRoutesByUserId(userId: String) : Flowable<Map<Long, List<Route>>> {
        val databaseReference = firebaseDatabase.reference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_ROUTES)
            .orderByKey()

        return Flowable.create({ emitter ->
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val routeHolder = HashMap<Long, List<Route>>()
                    dataSnapshot.children.forEach { childList ->
                        val routeList = mutableListOf<Route>()
                        childList.children.forEach { child ->
                            val route = child.getValue(Route::class.java)
                            route?.let {
                                routeList.add(route)
                            }
                        }
                        routeHolder[childList.key!!.toLong()] = routeList
                    }
                    emitter.onNext(routeHolder)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }, BackpressureStrategy.BUFFER)
    }
}
