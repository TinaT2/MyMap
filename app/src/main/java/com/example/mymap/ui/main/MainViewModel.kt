package com.example.mymap.ui.main

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymap.data.model.RouteDto
import com.example.mymap.data.repository.RoutingRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
interface RoutUiState {
    val routeDto: RouteDto?
}

private class MutableRoutUiState : RoutUiState {
    override var routeDto: RouteDto? by mutableStateOf(null)
}

@HiltViewModel
class MainViewModel @Inject constructor(private val routingRepository: RoutingRepository) :
    ViewModel() {

    private val _uiState: MutableRoutUiState = MutableRoutUiState()
    val uiState: RoutUiState = _uiState

    fun getRouting(latLng: Pair<LatLng, LatLng>) {
        viewModelScope.launch {
            try {
                val coordinates =
                    latLng.first.longitude.toString() + "," + latLng.first.latitude + ";" + latLng.second.longitude + "," + latLng.second.latitude
                Log.d("getRouting coordinates", coordinates)
                val response = routingRepository.getRout(coordinates)
                Log.d("getRouting response", response.toString())
                _uiState.routeDto = response
            }catch (exception: Exception){
                Log.e("getRouting error", exception.message.toString())
            }
        }
    }

    fun clearRouteDto(){
        _uiState.routeDto = null
    }
}