package com.whisker.mrr.xrunner.domain.source

import io.reactivex.Completable
import io.reactivex.Single

interface UserSource {

    fun getUserId() : Single<String>
    fun login(email: String, password: String) : Completable
    fun createAccount(email:String, password: String) : Completable
}