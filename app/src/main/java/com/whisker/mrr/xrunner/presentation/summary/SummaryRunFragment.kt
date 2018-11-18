package com.whisker.mrr.xrunner.presentation.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.presentation.BaseFragment
import com.whisker.mrr.xrunner.utils.xRunnerConstants

class SummaryRunFragment : BaseFragment() {

    private lateinit var viewModel: SummaryRunViewModel
    private lateinit var finalRoute: Route

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null) {
            finalRoute = arguments?.getParcelable(xRunnerConstants.EXTRA_FINAL_ROUTE_KEY)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_summary_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = mainActivity.run {
            ViewModelProviders.of(this, viewModelFactory).get(SummaryRunViewModel::class.java)
        }
    }
}