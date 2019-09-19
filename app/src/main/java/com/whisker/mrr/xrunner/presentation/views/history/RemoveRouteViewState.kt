package com.whisker.mrr.xrunner.presentation.views.history

sealed class RemoveRouteViewState {
    class Error(val msg: String?) : RemoveRouteViewState()
    class RouteRemoved(val routeID: Long) : RemoveRouteViewState()
}