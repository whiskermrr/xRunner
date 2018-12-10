package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.database.*
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.utils.xRunnerConstants.REFERENCE_ROUTES
import com.whisker.mrr.xrunner.utils.xRunnerConstants.REFERENCE_USERS
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class RouteDatabaseSource @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {

    fun saveRoute(route: Route, userId : String) : Completable {
        val databaseReference = firebaseDatabase.reference.child(REFERENCE_USERS).child(userId).child(REFERENCE_ROUTES)
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

    fun getRoutesByUserId(userId: String) : Flowable<List<Route>> {
        val databaseReference = firebaseDatabase.reference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_ROUTES)
            .orderByKey()

        return Flowable.create({ emitter ->
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val routeList = mutableListOf<Route>()
                    dataSnapshot.children.forEach { child ->
                        val route = child.getValue(Route::class.java)
                        route?.let {
                            routeList.add(route)
                        }
                    }
                    emitter.onNext(routeList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }, BackpressureStrategy.BUFFER)
    }
}
