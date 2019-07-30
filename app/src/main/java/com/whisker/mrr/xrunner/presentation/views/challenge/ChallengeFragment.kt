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
import com.whisker.mrr.xrunner.presentation.adapters.PaddingItemDecoration
import com.whisker.mrr.xrunner.presentation.adapters.ChallengeSection
import com.whisker.mrr.xrunner.presentation.views.BaseFragment
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_challenges.*

class ChallengeFragment : BaseFragment() {

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var challengeAdapter: SectionedRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.toolbar.title = getString(R.string.title_challenges)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChallengeViewModel::class.java)


        challengeAdapter = SectionedRecyclerViewAdapter()
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(challengeAdapter.getSectionItemViewType(position)) {
                    SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER -> 2
                    else -> 1
                }
            }
        }
        rvChallenges.layoutManager = gridLayoutManager
        rvChallenges.adapter = challengeAdapter
        rvChallenges.addItemDecoration(PaddingItemDecoration(resources.getDimensionPixelOffset(R.dimen.challenge_grid_spacing)))

        viewModel.getChallengeList().observe(this, Observer { challengeHolder ->
            progressBar.visibility = View.GONE
            challengeAdapter.removeAllSections()

            if(!challengeHolder.activeChallenges.isNullOrEmpty()) {
                challengeAdapter.addSection(ChallengeSection(true, challengeHolder.activeChallenges))
            }

            if(!challengeHolder.finishedChallenges.isNullOrEmpty()) {
                challengeAdapter.addSection(ChallengeSection(false, challengeHolder.finishedChallenges))
            }

            challengeAdapter.notifyDataSetChanged()
        })

        fabAddChallenge.setOnClickListener {
            showAddChallengeDialog()
        }
    }

    private fun showAddChallengeDialog() {
        mainActivity.addContent(AddChallengeDialogFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN, true)
    }
}