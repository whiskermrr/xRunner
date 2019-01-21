package com.whisker.mrr.xrunner.di

import com.whisker.mrr.xrunner.infrastructure.SnapshotSyncService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SyncServiceModule(private val service: SnapshotSyncService) {

    @Provides
    @Singleton
    fun provideService() = service
}