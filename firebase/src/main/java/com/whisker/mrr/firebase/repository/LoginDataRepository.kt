package com.whisker.mrr.firebase.repository

import com.whisker.mrr.domain.repository.LoginRepository
import com.whisker.mrr.data.source.AuthSource
import io.reactivex.Completable
import io.reactivex.Single

class LoginDataRepository(private val authDataSource: AuthSource) : LoginRepository {

    override fun login(email: String, password: String) : Completable {
        return authDataSource.login(email, password)
    }

    override fun createAccount(email: String, password: String) : Single<String> {
        return authDataSource.createAccount(email, password)
    }
}