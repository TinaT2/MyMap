package com.example.mymap.data.repository

import com.example.mymap.data.api.ApiInterfaceMapIr
import javax.inject.Inject


class RoutingRepository @Inject constructor(private val apiInterfaceMapIr: ApiInterfaceMapIr){

    suspend fun getRout(coordinates: String) = apiInterfaceMapIr.getRoute(coordinates)
}