package com.whisker.mrr.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.whisker.mrr.domain.source.AuthSource
import io.reactivex.Completable
import javax.inject.Inject

class AuthDataSource
@Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthSource {

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

    override fun createAccount(email:String, password: String) : Completable {
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