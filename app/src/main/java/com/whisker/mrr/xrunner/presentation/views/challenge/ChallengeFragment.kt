package com.whisker.mrr.xrunner.presentation.views.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.BaseFragment

class ChallengeFragment : BaseFragment() {

    private lateinit var viewModel: ChallengeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChallengeViewModel::class.java)

        viewModel.getChallengeList().observe(this, Observer {
            Toast.makeText(context, it.size.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}