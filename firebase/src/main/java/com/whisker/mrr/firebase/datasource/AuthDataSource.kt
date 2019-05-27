package com.whisker.mrr.firebase.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.whisker.mrr.domain.source.AuthSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
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
        }.observeOn(Schedulers.io())
    }

    override fun createAccount(email:String, password: String) : Single<String> {
        return Single.create<String> { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    Log.e("RXCHAIN", Thread.currentThread().name)
                    if(task.isSuccessful && task.result != null) {
                        emitter.onSuccess(task.result!!.user.uid)
                    } else if(task.exception != null) {
                        emitter.onError(task.exception!!)
                    } else {
                        emitter.onError(Throwable("Unknown error."))
                    }
                }
        }.observeOn(Schedulers.io())
    }
}