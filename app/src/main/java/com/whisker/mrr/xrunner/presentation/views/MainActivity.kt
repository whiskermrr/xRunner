package com.whisker.mrr.xrunner.presentation.views

import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.di.Injectable
import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.common.bus.event.SyncEvent
import com.whisker.mrr.infrastructure.NetworkStateReceiver
import com.whisker.mrr.xrunner.presentation.views.base.BaseActivity
import com.whisker.mrr.xrunner.presentation.views.challenge.ChallengeFragment
import com.whisker.mrr.xrunner.presentation.views.history.PastRoutesFragment
import com.whisker.mrr.xrunner.presentation.views.login.LoginFragment
import com.whisker.mrr.xrunner.presentation.views.map.RunFragment
import com.whisker.mrr.xrunner.presentation.views.profile.UserProfileFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), Injectable, HasSupportFragmentInjector {

    @Inject
    @SuppressWarnings("WeakerAccess")
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var networkStateReceiver: NetworkStateReceiver

    private val menuItems = listOf(R.id.action_run, R.id.action_challenges, R.id.action_past_routes, R.id.action_account)
    var isBottomNavEnabled = true

    private val syncConsumer = Consumer<Any> {
        if(it is SyncEvent) {
            if(it.isSyncRunning) {
                tvSync.visibility = View.VISIBLE
            } else {
                tvSync.visibility = View.GONE
            }
        }
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        if(FirebaseAuth.getInstance().currentUser != null) {
            loggedIn()
        } else {
            hideBottomNavigation()
            supportFragmentManager.beginTransaction()
                .add(mainContainer.id, LoginFragment())
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        RxBus.subscribe(SyncEvent::class.java.name, this, syncConsumer)
        registerReceiver(networkStateReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        networkStateReceiver.onReceive(this, null)
    }

    fun loggedIn() {
        showBottomNavigation()
        navBottom.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_run -> {
                    navigateToFragment(RunFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_past_routes -> {
                    navigateToFragment(PastRoutesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_account -> {
                    navigateToFragment(UserProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_challenges -> {
                    navigateToFragment(ChallengeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        navBottom.selectedItemId = R.id.action_run
    }

    private fun navigateToFragment(fragment: Fragment) {
        if(isFragmentInBackStack(fragment.javaClass.name)) {
            popBackStackToFragment(fragment.javaClass.name)
        } else {
            clearBackStack()
            switchContent(fragment)
        }
    }

    fun showToolbar() {
        toolbar.visibility = View.VISIBLE
    }

    fun hideToolbar() {
        toolbar.visibility = View.GONE
    }

    fun showBottomNavigation() {
        navBottom.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        navBottom.visibility = View.GONE
    }

    fun disableBottomNavigation() {
        isBottomNavEnabled = false
        for(item in menuItems) {
            if(item != navBottom.selectedItemId) {
                navBottom.menu.findItem(item).isEnabled = false
            }
        }
    }

    fun enableBottomNavigation() {
        isBottomNavEnabled = true
        for(item in menuItems) {
            navBottom.menu.findItem(item).isEnabled = true
        }
    }

    fun switchContent(fragment: Fragment) {
        switchContent(mainContainer.id, fragment)
    }

    fun addContent(fragment: Fragment, @IdRes transaction: Int = FragmentTransaction.TRANSIT_FRAGMENT_FADE, isAddToBackStack: Boolean = true) {
        addContent(mainContainer.id, fragment, transaction, isAddToBackStack)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

    override fun onStop() {
        super.onStop()
        RxBus.unsubscribe(this)
    }
}
