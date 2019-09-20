package com.whisker.mrr.xrunner.presentation.views.history

import com.whisker.mrr.xrunner.presentation.model.RouteModel

sealed class RemoveRouteViewState {
    class Error(val msg: String?) : RemoveRouteViewState()
    class RouteRemoved(val route: RouteModel, val position: Int) : RemoveRouteViewState()
}