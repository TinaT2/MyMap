package com.example.mymap.util

import com.example.mymap.data.model.Route
import com.example.mymap.data.model.RouteDto
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

object LatLngUtil {

    fun decodeRouteToLatLng(routeDto: RouteDto): List<Pair<Route,List<LatLng>>> {
        val routeList = mutableListOf<Pair<Route,List<LatLng>>>()
        routeDto.routes.forEach { route ->
            val allPoints = mutableListOf<LatLng>()
            route.legs.first().steps.forEach { step ->
                val decoded = PolyUtil.decode(step.geometry)
                allPoints.addAll(decoded)
            }
            routeList.add(route to allPoints)
        }
        return routeList
    }
}