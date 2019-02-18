package com.whisker.mrr.xrunner.presentation.views.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.adapters.ChallengeItemDecoration
import com.whisker.mrr.xrunner.presentation.adapters.ChallengesAdapter
import com.whisker.mrr.xrunner.presentation.views.BaseFragment
import kotlinx.android.synthetic.main.fragment_challenges.*

class ChallengeFragment : BaseFragment() {

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var challengeAdapter: ChallengesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChallengeViewModel::class.java)

        val gridLayoutManager = GridLayoutManager(context, 2)
        rvChallenges.layoutManager = gridLayoutManager
        challengeAdapter = ChallengesAdapter()
        rvChallenges.adapter = challengeAdapter
        rvChallenges.addItemDecoration(ChallengeItemDecoration(resources.getDimensionPixelOffset(R.dimen.challenge_grid_spacing)))

        viewModel.getChallengeList().observe(this, Observer {
            progressBar.visibility = View.GONE
            challengeAdapter.setItems(it)
        })

        fabAddChallenge.setOnClickListener {
            showAddChallengeDialog()
        }
    }

    private fun showAddChallengeDialog() {
        mainActivity.addContent(AddChallengeDialogFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN, true)
    }
}