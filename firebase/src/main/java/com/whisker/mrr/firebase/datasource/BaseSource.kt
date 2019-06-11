package com.whisker.mrr.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.whisker.mrr.domain.common.exception.NoConnectivityException
import io.reactivex.Completable

open class BaseSource(
    private val firebaseAuth: FirebaseAuth
) {

    protected var isNetworkAvailable = false

    protected fun getUserID() : String {
        var userUID = ""
        firebaseAuth.currentUser?.uid?.let { userUID = it } ?: run { throw FirebaseAuthInvalidUserException("", "") }
        return userUID
    }

    protected fun checkConnection() : Completable {
        return Completable.create { emitter ->
            if(isNetworkAvailable) {
                emitter.onComplete()
            } else {
                emitter.onError(NoConnectivityException())
            }
        }
    }
}