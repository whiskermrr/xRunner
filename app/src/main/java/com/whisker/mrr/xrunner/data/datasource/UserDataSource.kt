package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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
}