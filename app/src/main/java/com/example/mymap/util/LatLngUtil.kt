package com.example.mymap.util

import com.example.mymap.data.model.RouteDto
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

object LatLngUtil {

    fun decodeRouteToLatLng(routeDto: RouteDto): List<LatLng> {
        val allPoints = mutableListOf<LatLng>()
        routeDto.routes.firstOrNull()?.legs?.forEach { leg ->
            leg.steps.forEach { step ->
                val decoded = PolyUtil.decode(step.geometry)
                allPoints.addAll(decoded)
            }
        }
        return allPoints
    }
}