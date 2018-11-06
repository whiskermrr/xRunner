package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.database.FirebaseDatabase
import com.whisker.mrr.xrunner.domain.model.Route
import io.reactivex.Completable
import javax.inject.Inject

class DatabaseSource @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {

    fun saveRoute(route: Route, userId : String) : Completable {
        val databaseReference = firebaseDatabase.reference.child("Users").child(route.name).child(userId)
        databaseReference.setValue(route)
        return Completable.complete()
    }
}
