package com.whisker.mrr.xrunner.presentation.views

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

open class BaseActivity : AppCompatActivity() {


    protected fun switchContent(@IdRes frameLayoutContainer: Int, fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(frameLayoutContainer, fragment, fragment.javaClass.name)
        ft.addToBackStack(fragment.javaClass.name)
        ft.commit()
    }

    private fun getTopFragment(manager: FragmentManager): Fragment? {
        var previousFragment: Fragment? = null
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

    fun addContent(
        @IdRes frameLayoutContainer: Int,
        fragment: Fragment,
        @IdRes transaction: Int = FragmentTransaction.TRANSIT_FRAGMENT_FADE,
        isAddToBackStack: Boolean = true) {

        val fragmentTag = fragment.javaClass.name
        val manager = supportFragmentManager
        val previousFragment = getTopFragment(manager)
        val ft = manager.beginTransaction()
        ft.setTransition(transaction)
        if (previousFragment != null) {
            ft.hide(previousFragment)
        }
        ft.add(frameLayoutContainer, fragment, fragmentTag)
        if(isAddToBackStack) {
            ft.addToBackStack(fragmentTag)
        } else {
            ft.addToBackStack(null)
        }
        ft.commit()
    }
}