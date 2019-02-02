package com.whisker.mrr.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.whisker.mrr.domain.source.AuthSource
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AuthDataSource
@Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthSource {

    override fun getUserId() : Single<String> {
        if(firebaseAuth.currentUser != null) {
            return Single.just(firebaseAuth.currentUser!!.uid)
        }
        return Single.error(FirebaseAuthInvalidUserException("", ""))
    }

    override fun login(email: String, password: String) : Completable {
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

    override fun createAccount(email:String, password: String) : Single<String> {
        return Single.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {

                        emitter.onSuccess(task.result!!.user.uid)
                    } else if(task.exception != null) {
                        emitter.onError(task.exception!!)
                    }
                }
        }
    }
}