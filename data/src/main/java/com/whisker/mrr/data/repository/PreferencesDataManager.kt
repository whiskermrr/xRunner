package com.whisker.mrr.data.repository

import com.whisker.mrr.data.database.dao.PreferencesDao
import com.whisker.mrr.data.database.model.Preferences
import com.whisker.mrr.domain.manager.PreferencesManager
import io.reactivex.Completable
import io.reactivex.Single

class PreferencesDataManager(private val preferencesDao: PreferencesDao) : PreferencesManager {

    override fun saveValue(key: String, value: String): Completable {
        return Completable.fromAction {
            preferencesDao.insert(Preferences(key, value, null, null))
        }
    }

    override fun saveValue(key: String, value: Long): Completable {
        return Completable.fromAction {
            preferencesDao.insert(Preferences(key, null, value, null))
        }
    }

    override fun saveValue(key: String, value: Boolean): Completable {
        return Completable.fromAction {
            preferencesDao.insert(Preferences(key, null, null, value))
        }
    }

    override fun getStringValue(key: String): Single<String> {
        return preferencesDao.getStringValue(key).onErrorReturn { "" }
    }

    override fun getLongValue(key: String): Single<Long> {
        return preferencesDao.getLongValue(key).onErrorReturn { 0 }
    }

    override fun getBooleanValue(key: String): Single<Boolean> {
        return preferencesDao.getBooleanValue(key).onErrorReturn { false }
    }

    override fun deleteKey(key: String): Completable {
        return Completable.fromAction {
            preferencesDao.deleteValue(key)
        }
    }

    override fun deleteKeyList(keys: List<String>): Completable {
        return Completable.fromAction {
            preferencesDao.deleteValues(keys)
        }
    }
}