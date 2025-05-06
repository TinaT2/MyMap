package com.example.mymap.data.api

import com.example.mymap.data.model.RouteDto
import retrofit2.http.HTTP
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterfaceMapIr {

    @HTTP(method = "GET", path = "route/v1/driving/{coordinates}")
    suspend fun getRoute(
        @Path("coordinates") coordinates: String,
        @Query("steps") steps: String = "true"
    ): RouteDto

}