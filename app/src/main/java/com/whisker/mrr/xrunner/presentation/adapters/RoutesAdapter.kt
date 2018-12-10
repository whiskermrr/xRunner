package com.whisker.mrr.xrunner.presentation.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.utils.loadSnapshot
import org.jetbrains.anko.layoutInflater
import java.util.*

class RoutesAdapter : RecyclerView.Adapter<RoutesAdapter.RouteViewHolder>() {

    private var routes: List<Route> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view: View = parent.context.layoutInflater.inflate(R.layout.route_list_item, parent, false)
        return RouteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return routes.size
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(routes[position])
    }

    fun setRoutes(routes: List<Route>) {
        this.routes = routes
        notifyDataSetChanged()
    }

    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var ivRouteSnapshot: ImageView = itemView.findViewById(R.id.ivRouteSnapshot)
        private var tvRouteName: TextView = itemView.findViewById(R.id.tvRouteName)
        private var tvRouteDescription: TextView = itemView.findViewById(R.id.tvRouteDescription)

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
}