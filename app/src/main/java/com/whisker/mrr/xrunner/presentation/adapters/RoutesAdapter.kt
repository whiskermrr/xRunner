package com.whisker.mrr.xrunner.presentation.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.utils.loadSnapshot
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection
import java.util.*

class RoutesAdapter(private var routes: List<Route>) :
    StatelessSection(SectionParameters.builder()
        .itemResourceId(R.layout.route_list_item)
        .headerResourceId(R.layout.route_list_header)
        .build()) {

    override fun getContentItemsTotal(): Int {
        return routes.size
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as RouteViewHolder).bind(routes[position])
    }

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        return RouteViewHolder(view!!)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        super.onBindHeaderViewHolder(holder)
    }

    override fun getHeaderView(parent: ViewGroup?): View {
        return super.getHeaderView(parent)
    }

    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivRouteSnapshot: ImageView = itemView.findViewById(R.id.ivRouteSnapshot)
        private val tvRouteName: TextView = itemView.findViewById(R.id.tvRouteName)
        private val tvRouteDescription: TextView = itemView.findViewById(R.id.tvRouteDescription)

        fun bind(route: Route) {
            ivRouteSnapshot.loadSnapshot(route.date.toString())
            tvRouteName.text = route.name
            tvRouteDescription.text =
                    String.format(
                        Locale.getDefault(),
                        "%d.%03d km in %02d:%02d:%02d",
                        route.routeStats.kilometers,
                        route.routeStats.meters,
                        route.routeStats.hours,
                        route.routeStats.minutes,
                        route.routeStats.seconds
                        )
        }
    }

    inner class RouteHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMonthHeader: TextView = itemView.findViewById(R.id.tvMonthHeader)
        private val tvMonthDistance: TextView = itemView.findViewById(R.id.tvMonthDistance)
        private val tvMonthPeace: TextView = itemView.findViewById(R.id.tvMonthPeace)
        private val tvMonthTime: TextView = itemView.findViewById(R.id.tvMonthTime)

        fun bind(month: String, distance: String, peace: String, time: String) {
            tvMonthHeader.text = month
            tvMonthDistance.text = distance
            tvMonthPeace.text = peace
            tvMonthTime.text = time
        }
    }
}