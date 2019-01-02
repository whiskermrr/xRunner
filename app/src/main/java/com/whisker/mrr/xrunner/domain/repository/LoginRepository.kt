package com.whisker.mrr.xrunner.domain.repository

import io.reactivex.Completable
import io.reactivex.Single

interface LoginRepository {
    fun login(email: String, password: String) : Completable
    fun createAccount(email:String, password: String) : Single<String>
}