package com.whisker.mrr.domain.source

import io.reactivex.Completable

interface AuthSource {
    fun login(email: String, password: String) : Completable
    fun createAccount(email:String, password: String) : Completable
}