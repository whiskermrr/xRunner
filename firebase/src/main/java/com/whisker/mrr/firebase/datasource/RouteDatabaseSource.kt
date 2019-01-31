package com.whisker.mrr.firebase.datasource

import com.google.firebase.database.*
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_ROUTES
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.domain.common.getFirstDayOfTheMonthInMillis
import com.whisker.mrr.domain.model.RouteEntity
import com.whisker.mrr.domain.model.RouteEntityHolder
import com.whisker.mrr.domain.source.RouteSource
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*
import javax.inject.Inject

class RouteDatabaseSource @Inject constructor(private val firebaseDatabase: FirebaseDatabase) : RouteSource {

    override fun saveRoute(route: RouteEntity, userId : String) : Completable {
        val databaseReference = firebaseDatabase.reference
            .child(REFERENCE_USERS).child(userId)
            .child(REFERENCE_ROUTES)
            .child(Date(route.date).getFirstDayOfTheMonthInMillis().toString())

        route.routeId = databaseReference.push().key!!
        return Completable.create { emitter ->
            databaseReference.child(route.routeId).setValue(route).addOnCompleteListener { task ->
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

    override fun getRoutesByUserId(userId: String) : Flowable<List<RouteEntityHolder>> {
        val databaseReference = firebaseDatabase.reference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_ROUTES)

        return Flowable.create({ emitter ->
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val holders = mutableListOf<RouteEntityHolder>()
                    dataSnapshot.children.forEach { childList ->
                        val holder = RouteEntityHolder()
                        holder.month = childList.key!!.toLong()
                        childList.children.forEach { child ->
                            val route = child.getValue(RouteEntity::class.java)
                            route?.let {
                                holder.totalDistance += it.routeStats.wgs84distance
                                holder.totalTime += it.routeStats.routeTime
                                holder.routes.add(it)
                            }
                        }
                        holders.add(holder)
                    }
                    emitter.onNext(holders)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }, BackpressureStrategy.BUFFER)
    }

    override fun removeRouteById(userId: String, routeId: String, date: Long): Completable {
        val databaseReference = firebaseDatabase.reference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_ROUTES)
            .child(Date(date).getFirstDayOfTheMonthInMillis().toString())
            .child(routeId)

        return Completable.create { emitter ->
            databaseReference.removeValue().addOnCompleteListener { task ->
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
