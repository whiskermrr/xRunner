package com.whisker.mrr.xrunner.di

import com.whisker.mrr.xrunner.presentation.MainActivity
import com.whisker.mrr.xrunner.presentation.login.LoginFragment
import com.whisker.mrr.xrunner.presentation.map.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity() : MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindLoginFragment() : LoginFragment

    @ContributesAndroidInjector
    internal abstract fun bindMapFragment() : MapFragment
}