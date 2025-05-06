package com.example.mymap.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymap.BuildConfig
import com.example.mymap.ui.theme.MyMapTheme
import com.example.mymap.util.LatLngUtil.decodeRouteToLatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val accessToken = BuildConfig.MAP_IR_API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    MyMapRouteScreen()
                }
            }
        }
    }

    @Composable
    fun MyMapRouteScreen(mainViewModel: MainViewModel = hiltViewModel<MainViewModel>()) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(35.6892, 51.3890), 12f) // Tehran
        }

        var clickablePositionOne by remember { mutableStateOf<LatLng?>(null) }
        var clickablePositionTwo by remember { mutableStateOf<LatLng?>(null) }

        Box(modifier = Modifier.fillMaxSize()) {


            if (clickablePositionOne != null && clickablePositionTwo != null) {
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(100.dp)
                        .zIndex(10f),
                    onClick = {
                        mainViewModel.getRouting(clickablePositionOne!! to clickablePositionTwo!!)
                    }) {
                    Text(text = "Routing")
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLongClick = { latLng ->
                    if (clickablePositionOne == null)
                        clickablePositionOne = latLng
                    else if (clickablePositionTwo == null)
                        clickablePositionTwo = latLng
                    else {
                        clickablePositionOne = latLng
                        clickablePositionTwo = null
                    }
                },
                onMapClick = {
                    clickablePositionOne = null
                    clickablePositionTwo = null
                    mainViewModel.clearRouteDto()
                }
            ) {
                mainViewModel.uiState.routeDto?.let{
                   val polylinePoints = decodeRouteToLatLng(it)
                    Polyline(
                        points = polylinePoints,
                        color = Color.Blue,
                        width = 8f
                    )
                }


                clickablePositionOne?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Start Point",
                        snippet = "From here"
                    )
                }

                clickablePositionTwo?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "End Point",
                        snippet = "To Here"
                    )
                }

                MapEffect { map ->
                    // Set map type to none to remove default tiles
//todo make it back to business finally
// map.mapType = GoogleMap.MAP_TYPE_NONE
//
//                    // Add Map.ir tile overlay
//                    map.addTileOverlay(
//                        TileOverlayOptions().tileProvider(
//                            MapirUrlTileProvider(accessToken)
//                        )
//                    )
                }
            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMapTheme {
        Greeting("Android")
    }
}