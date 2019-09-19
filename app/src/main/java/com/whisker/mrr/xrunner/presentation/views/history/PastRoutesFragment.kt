package com.whisker.mrr.xrunner.presentation.views.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.base.BaseFragment
import com.whisker.mrr.xrunner.presentation.adapters.RoutesSection
import com.whisker.mrr.xrunner.presentation.common.SwipeToDeleteCallback
import com.whisker.mrr.xrunner.presentation.model.RouteHolderModel
import com.whisker.mrr.xrunner.utils.TAG
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_past_routes.*

class PastRoutesFragment : BaseFragment() {

    private lateinit var viewModel: PastRoutesViewModel
    private lateinit var routesAdapter: SectionedRecyclerViewAdapter
    private lateinit var swipeHandler: SwipeToDeleteCallback
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_past_routes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.toolbar.title = getString(R.string.title_history)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PastRoutesViewModel::class.java)

        initRecyclerView()
        initSwipeToDeleteHandler()
        initTouchHelper()

        viewModel.getRouteList().observe(viewLifecycleOwner, Observer { viewState ->
            if(swipeRoutesLayout.isRefreshing) {
                swipeRoutesLayout.isRefreshing = false
            }

            when(viewState) {
                is GetRoutesViewState.Routes -> {
                    setRouteSections(viewState.holders)
                }
                is GetRoutesViewState.Error -> {
                    // TODO: dialog showing error
                    Log.e(TAG(), viewState.msg ?: "Unknown Error")
                }
            }
        })

        viewModel.getRouteRemoved().observe(viewLifecycleOwner, Observer { viewState ->
            when(viewState) {
                is RemoveRouteViewState.RouteRemoved -> {

                }
            }
        })

        swipeRoutesLayout.isRefreshing = true
        swipeRoutesLayout.setOnRefreshListener {
            viewModel.getPastRoutesList()
        }
    }

    private fun initRecyclerView() {
        routesAdapter = SectionedRecyclerViewAdapter()
        rvRoutes.layoutManager = LinearLayoutManager(context)
        rvRoutes.adapter = routesAdapter
    }

    private fun initSwipeToDeleteHandler() {
        swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val section = routesAdapter.getSectionForPosition(viewHolder.adapterPosition) as RoutesSection
                val globalPosition = routesAdapter.getPositionInAdapter(section, 0)
                val route = section.removeItem(viewHolder.adapterPosition - globalPosition)

                routesAdapter.notifyItemRemovedFromSection(section, viewHolder.adapterPosition - globalPosition)
                if(section.contentItemsTotal == 0) {
                    routesAdapter.removeSection(section)
                    routesAdapter.notifyDataSetChanged()
                }

                viewModel.removeRoute(route.routeId)
            }
        }
    }

    private fun initTouchHelper() {
        itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvRoutes)
    }

    private fun setRouteSections(holders: List<RouteHolderModel>) {
        if(holders.isNotEmpty()) {
            tvRouteNoResult.visibility = View.GONE
            routesAdapter.removeAllSections()
            for(holder in holders) {
                routesAdapter.addSection(RoutesSection(holder))
                routesAdapter.notifyDataSetChanged()
            }
        } else {
            tvRouteNoResult.visibility = View.VISIBLE
        }
    }
}