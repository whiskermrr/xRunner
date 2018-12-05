package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.database.*
import com.whisker.mrr.xrunner.domain.model.Route
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class RouteDatabaseSource @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {

    fun saveRoute(route: Route, userId : String) : Completable {
        val databaseReference = firebaseDatabase.reference.child("Users").child(userId)
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
        val databaseReference = firebaseDatabase.reference.child("Users").child(userId)
        return Flowable.create({ emitter ->
            databaseReference.addChildEventListener(object: ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }, BackpressureStrategy.BUFFER)
    }
}
