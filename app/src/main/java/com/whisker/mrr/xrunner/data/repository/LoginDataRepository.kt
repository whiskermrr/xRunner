package com.whisker.mrr.xrunner.data.repository

import com.whisker.mrr.xrunner.domain.repository.LoginRepository
import com.whisker.mrr.xrunner.domain.source.UserSource
import io.reactivex.Completable

class LoginDataRepository(private val userDataSource: UserSource) : LoginRepository {

    override fun login(email: String, password: String) : Completable {
        return userDataSource.login(email, password)
    }

    override fun createAccount(email: String, password: String) : Completable {
        return userDataSource.createAccount(email, password)
    }
}