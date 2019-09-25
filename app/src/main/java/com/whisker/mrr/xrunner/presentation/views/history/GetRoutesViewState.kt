package com.whisker.mrr.xrunner.presentation.views.history

import com.whisker.mrr.xrunner.presentation.model.RouteHolderModel

sealed class GetRoutesViewState {
    class Error(val msg: String?) : GetRoutesViewState()
    class Routes(val holders: List<RouteHolderModel>) : GetRoutesViewState()
}