package com.whisker.mrr.xrunner.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.di.Injectable
import com.whisker.mrr.xrunner.presentation.login.LoginFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable, HasSupportFragmentInjector {

    @Inject
    @SuppressWarnings("WeakerAccess")
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.mainContainer, LoginFragment())
            .commit()
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector
}
