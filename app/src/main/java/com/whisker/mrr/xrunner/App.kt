package com.whisker.mrr.xrunner

import com.whisker.mrr.xrunner.di.DaggerAppComponent
import com.whisker.mrr.xrunner.di.applyAutoInjector
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        this.applyAutoInjector()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}