package com.whisker.mrr.xrunner.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.whisker.mrr.xrunner.domain.repository.LoginRepository
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe

class LoginDataRepository(private val mAuth: FirebaseAuth) : LoginRepository {

    override fun login(email: String, password: String) : Maybe<AuthResult> {
        return RxFirebaseAuth.signInWithEmailAndPassword(mAuth, email, password)
    }

    override fun createAccount(email:String, password: String) : Maybe<AuthResult> {
        return RxFirebaseAuth.createUserWithEmailAndPassword(mAuth, email, password)
    }
}