package com.example.mymap.data.model

data class RouteDto(
    val code: String,
    val routes: List<Route>,
    val waypoints: List<Waypoint>
)

data class Route(
    val distance: Double,
    val duration: Double,
    val legs: List<Leg>
)

data class Leg(
    val steps: List<Step>,
    val summary: String,
    val distance: Double,
    val duration: Double,
)

data class Step(
    val name: String,
    val distance: Double,
    val duration: Double,
    val geometry: String,
    val mode: String,
    val maneuver: Maneuver
)

data class Maneuver(
    val instruction: String,
    val type: String,
    val location: List<Double> // [lon, lat]
)

data class Waypoint(
    val name: String,
    val location: List<Double> // [lon, lat]
)