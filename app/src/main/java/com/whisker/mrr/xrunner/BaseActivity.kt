package com.whisker.mrr.xrunner

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

open class BaseActivity : AppCompatActivity() {


    protected fun switchContent(@IdRes frameLayoutContainer: Int, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(frameLayoutContainer, fragment, fragment.javaClass.name)
        ft.addToBackStack(fragment.javaClass.name)
        ft.commit()
    }

    protected fun addContent(@IdRes frameLayoutContainer: Int, fragment: androidx.fragment.app.Fragment) {
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

    fun isFragmentInBackStack(fragmentName: String) : Boolean {
        return supportFragmentManager.popBackStackImmediate(fragmentName, 0)
    }

    fun popBackStackToFragment(fragmentName: String?) {
        supportFragmentManager.popBackStack(fragmentName, 0)
    }

    fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}