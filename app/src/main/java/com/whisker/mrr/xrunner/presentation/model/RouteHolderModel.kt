package com.whisker.mrr.xrunner.presentation.model

data class RouteHolderModel(
    var month: String = "",
    var totalDistance: String = "",
    var totalTime: String = "",
    var averagePace: String = "",
    var routes: MutableList<RouteModel> = mutableListOf()
)