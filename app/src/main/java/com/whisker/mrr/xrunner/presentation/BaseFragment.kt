package com.whisker.mrr.xrunner.presentation

import android.content.Context
import android.os.Bundle
import com.whisker.mrr.xrunner.di.Injectable
import com.whisker.mrr.xrunner.di.ViewModelFactory
import javax.inject.Inject

open class BaseFragment : androidx.fragment.app.Fragment(), Injectable {
    companion object {
        val TAG = this::class.java.simpleName
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var mainActivity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
}