package com.whisker.mrr.xrunner

import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.facebook.stetho.Stetho
import com.whisker.mrr.xrunner.di.DaggerAppComponent
import com.whisker.mrr.xrunner.di.applyAutoInjector
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { Log.e("xRunner", "error after termination") }
        this.applyAutoInjector()
        Stetho.initializeWithDefaults(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val androidInjector = DaggerAppComponent.builder()
            .application(this)
            .build()

        val factory = androidInjector.workerFactory()
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(factory).build())

        return androidInjector
    }
}