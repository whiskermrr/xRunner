package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserDataSource
@Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun getUserId() : Single<String> {
        if(firebaseAuth.currentUser != null) {
            return Single.just(firebaseAuth.currentUser!!.uid)
        }
        return Single.error(FirebaseAuthInvalidUserException("", ""))
    }

    fun login(email: String, password: String) : Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        emitter.onComplete()
                    } else if(task.exception != null) {
                        emitter.onError(task.exception!!)
                    }
                }
        }
    }

    fun createAccount(email:String, password: String) : Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        emitter.onComplete()
                    } else if(task.exception != null) {
                        emitter.onError(task.exception!!)
                    }
                }
        }
    }
}