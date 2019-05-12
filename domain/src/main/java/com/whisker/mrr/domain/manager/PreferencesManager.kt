package com.whisker.mrr.domain.manager

import io.reactivex.Completable
import io.reactivex.Single

interface PreferencesManager {
    fun saveValue(key: String, value: String) : Completable
    fun saveValue(key: String, value: Long) : Completable
    fun saveValue(key: String, value: Boolean) : Completable

    fun getStringValue(key: String) : Single<String>
    fun getLongValue(key: String) : Single<Long>
    fun getBooleanValue(key: String) : Single<Boolean>

    fun deleteKey(key: String) : Completable
    fun deleteKeyList(keys: List<String>) : Completable
}