package com.whisker.mrr.xrunner.domain

import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

interface LoginRepository {
    fun login(email: String, password: String) : Maybe<AuthResult>
    fun createAccount(email:String, password: String) : Maybe<AuthResult>
}