package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe
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

    fun login(email: String, password: String) : Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(firebaseAuth, email, password)
    }

    fun createAccount(email:String, password: String) : Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(firebaseAuth, email, password)
    }
}