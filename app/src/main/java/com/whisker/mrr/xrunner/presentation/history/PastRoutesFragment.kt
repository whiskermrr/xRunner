package com.whisker.mrr.xrunner.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.BaseFragment
import com.whisker.mrr.xrunner.presentation.adapters.RoutesAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_past_routes.*

class PastRoutesFragment : BaseFragment() {

    private lateinit var viewModel: PastRoutesViewModel
    private lateinit var routesAdapter: SectionedRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_past_routes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PastRoutesViewModel::class.java)

        routesAdapter = SectionedRecyclerViewAdapter()
        rvRoutes.layoutManager = LinearLayoutManager(context)
        rvRoutes.adapter = routesAdapter

        viewModel.getRouteList().observe(this, Observer {routes ->
            routesAdapter.addSection(RoutesAdapter(routes.values.first()))
        })
    }
}