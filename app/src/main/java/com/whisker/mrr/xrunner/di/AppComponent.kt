package com.whisker.mrr.xrunner.di

import android.app.IntentService
import android.app.Service
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.xrunner.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    BuildersModule::class])

interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent

    }

    override fun inject(app: App)

    fun inject(mainActivity: MainActivity)
    fun inject(service: Service)
    fun inject(service: IntentService)
}