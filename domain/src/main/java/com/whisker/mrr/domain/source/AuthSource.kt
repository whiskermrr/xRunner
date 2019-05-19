package com.whisker.mrr.domain.source

import io.reactivex.Completable
import io.reactivex.Single

interface AuthSource {
    fun login(email: String, password: String) : Completable
    fun createAccount(email:String, password: String) : Single<String>
}