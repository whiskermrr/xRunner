package com.whisker.mrr.domain.repository

import io.reactivex.Completable

interface LoginRepository {
    fun login(email: String, password: String) : Completable
    fun createAccount(email:String, password: String) : Completable
}