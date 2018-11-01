package com.whisker.mrr.xrunner.domain.bus.event

import com.whisker.mrr.xrunner.domain.model.RoutePoint

data class LocationEvent(val location: RoutePoint)