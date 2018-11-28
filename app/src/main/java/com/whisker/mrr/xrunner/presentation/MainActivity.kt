package com.whisker.mrr.xrunner.presentation

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.di.Injectable
import com.whisker.mrr.xrunner.infrastructure.NetworkStateReceiver
import com.whisker.mrr.xrunner.presentation.login.LoginFragment
import com.whisker.mrr.xrunner.presentation.map.RunFragment
import com.whisker.mrr.xrunner.utils.xRunnerConstants
import com.whisker.mrr.xrunner.utils.xRunnerConstants.REQUEST_LOCATION_CODE
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable, HasSupportFragmentInjector {

    @Inject
    @SuppressWarnings("WeakerAccess")
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var networkStateReceiver: NetworkStateReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        if(FirebaseAuth.getInstance().currentUser != null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainContainer, RunFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainContainer, LoginFragment())
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkStateReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    fun switchContent(fragment: Fragment) {
        switchContent(mainContainer.id, fragment)
    }

    fun addContent(fragment: Fragment) {
        addContent(mainContainer.id, fragment)
    }

    private fun switchContent(@IdRes frameLayoutContainer: Int, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(frameLayoutContainer, fragment, fragment.javaClass.name)
        ft.addToBackStack(fragment.javaClass.name)
        ft.commit()
    }

    private fun addContent(@IdRes frameLayoutContainer: Int, fragment: androidx.fragment.app.Fragment) {
        val fragmentTag = fragment.javaClass.name
        val manager = supportFragmentManager
        val previousFragment = getTopFragment(manager)
        val ft = manager.beginTransaction()
        ft.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if (previousFragment != null) {
            ft.hide(previousFragment)
        }
        ft.add(frameLayoutContainer, fragment, fragmentTag)
        ft.addToBackStack(fragmentTag)
        ft.commit()
    }

    private fun getTopFragment(manager: androidx.fragment.app.FragmentManager): androidx.fragment.app.Fragment? {
        var previousFragment: androidx.fragment.app.Fragment? = null
        if (manager.backStackEntryCount > 0) {
            val backEntry = manager.getBackStackEntryAt(manager.backStackEntryCount - 1)
            previousFragment = supportFragmentManager.findFragmentByTag(backEntry.name)
        }
        return previousFragment
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            xRunnerConstants.REQUEST_LOCATION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun checkLocationPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
                )
            }
            return false
        }
        return true
    }

}
