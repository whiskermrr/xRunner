package com.whisker.mrr.xrunner.presentation.views.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.BaseFragment
import com.whisker.mrr.xrunner.presentation.adapters.RoutesAdapter
import com.whisker.mrr.xrunner.presentation.common.SwipeToDeleteCallback
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_past_routes.*

class PastRoutesFragment : BaseFragment() {

    private lateinit var viewModel: PastRoutesViewModel
    private lateinit var routesAdapter: SectionedRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_past_routes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.toolbar.title = getString(R.string.title_history)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PastRoutesViewModel::class.java)

        routesAdapter = SectionedRecyclerViewAdapter()
        rvRoutes.layoutManager = LinearLayoutManager(context)
        rvRoutes.adapter = routesAdapter

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val section = routesAdapter.getSectionForPosition(viewHolder.adapterPosition) as RoutesAdapter
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

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvRoutes)

        viewModel.getRouteList().observe(this, Observer {holders ->
            progressBar.visibility = View.GONE
            routesAdapter.removeAllSections()
            for(holder in holders) {
                routesAdapter.addSection(RoutesAdapter(holder))
                routesAdapter.notifyDataSetChanged()
            }
        })
    }
}