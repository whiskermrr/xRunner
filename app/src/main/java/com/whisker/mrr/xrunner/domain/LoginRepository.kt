package com.whisker.mrr.xrunner.domain

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe

class LoginRepository(private val mAuth: FirebaseAuth) {

    fun login(email: String, password: String) : Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(mAuth, email, password)
    }

    fun createAccount(email:String, password: String) : Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(mAuth, email, password)
    }
}