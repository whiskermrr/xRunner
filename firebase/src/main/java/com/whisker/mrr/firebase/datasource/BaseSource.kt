package com.whisker.mrr.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.whisker.mrr.domain.common.exception.NoConnectivityException
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

open class BaseSource(
    private val connectivityReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) {
    protected fun getUserID() : String {
        var userUID = ""
        firebaseAuth.currentUser?.uid?.let { userUID = it } ?: run { throw FirebaseAuthInvalidUserException("", "") }
        return userUID
    }

    protected fun checkConnection() : Completable {
        return Completable.create { emitter ->
            connectivityReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java) ?: false
                    if (connected) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(NoConnectivityException())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(NoConnectivityException())
                }
            })
        }.observeOn(Schedulers.io())
    }
}