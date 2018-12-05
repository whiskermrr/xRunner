package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.database.FirebaseDatabase
import com.whisker.mrr.xrunner.domain.model.Route
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class RouteDatabaseSource @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {

    fun saveRoute(route: Route, userId : String) : Completable {
        val databaseReference = firebaseDatabase.reference.child("Users").child(userId).child(route.name)
        databaseReference.setValue(route)
        return Completable.complete()
    }

    fun getRoutesByUserId(userId: String) : Flowable<List<Route>> {
        return Flowable.empty()
    }
}
