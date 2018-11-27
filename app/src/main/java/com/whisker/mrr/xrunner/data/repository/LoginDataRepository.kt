package com.whisker.mrr.xrunner.data.repository

import com.google.firebase.auth.AuthResult
import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.domain.repository.LoginRepository
import io.reactivex.Maybe

class LoginDataRepository(private val userDataSource: UserDataSource) : LoginRepository {

    override fun login(email: String, password: String) : Maybe<AuthResult> {
        return userDataSource.login(email, password)
    }

    override fun createAccount(email: String, password: String) : Maybe<AuthResult> {
        return userDataSource.createAccount(email, password)
    }
}