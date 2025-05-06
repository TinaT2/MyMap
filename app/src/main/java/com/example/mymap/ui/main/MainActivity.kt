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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymap.BuildConfig
import com.example.mymap.ui.theme.MyMapTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
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

    @OptIn(MapsComposeExperimentalApi::class)
    @Composable
    fun MyMapRouteScreen(hiltViewModel: MainViewModel = hiltViewModel<MainViewModel>()) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(35.6892, 51.3890), 12f) // Tehran
        }

        var polylinePoints = remember { mutableStateListOf<LatLng>() }

        var clickablePositionOne by remember { mutableStateOf<LatLng?>(null) }
        var clickablePositionTwo by remember { mutableStateOf<LatLng?>(null) }


        // Call route API and decode polyline
//        LaunchedEffect(Unit) {
//            val encoded = getMockMapIrPolyline() // Replace with real API call
//            polylinePoints.clear()
//            polylinePoints.addAll(decodePolyline(encoded))
//        }

        Box(modifier = Modifier.fillMaxSize()) {


            if (clickablePositionOne != null && clickablePositionTwo != null) {
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(100.dp)
                        .zIndex(10f),
                    onClick = {
                        hiltViewModel.getRouting(clickablePositionOne!! to clickablePositionTwo!!)
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
                }
            ) {
//        Polyline(
//            points = polylinePoints,
//            color = Color.Blue,
//            width = 8f
//        )

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

    fun getMockMapIrPolyline(): String {
        return "yzocFzynhVq}@n}@o}@nzD" // Replace with API call
    }


    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0

            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)

            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0

            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)

            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            poly.add(LatLng(lat / 1E5, lng / 1E5))
        }

        return poly
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