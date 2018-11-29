package com.whisker.mrr.xrunner.data.repository

import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.domain.repository.LoginRepository
import io.reactivex.Completable

class LoginDataRepository(private val userDataSource: UserDataSource) : LoginRepository {

    override fun login(email: String, password: String) : Completable {
        return userDataSource.login(email, password)
    }

    override fun createAccount(email: String, password: String) : Completable {
        return userDataSource.createAccount(email, password)
    }
}