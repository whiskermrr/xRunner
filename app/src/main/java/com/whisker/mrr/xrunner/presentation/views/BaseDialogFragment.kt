package com.whisker.mrr.xrunner.presentation.views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.whisker.mrr.xrunner.di.Injectable
import com.whisker.mrr.xrunner.di.ViewModelFactory
import javax.inject.Inject

open class BaseDialogFragment : DialogFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var mainActivity: MainActivity


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomNavigation()
    }
}