package com.example.mymap.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymap.R
import com.example.mymap.data.model.Leg
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
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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

            mainViewModel.uiState.routeDto?.apply {
                ButtonPanel(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    leg = routes.first().legs.first()
                ) {
                    clickablePositionOne = null
                    clickablePositionTwo = null
                    mainViewModel.clearRouteDto()
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLongClick = { latLng ->
                    if (clickablePositionOne == null)
                        clickablePositionOne = latLng
                    else if (clickablePositionTwo == null) {
                        clickablePositionTwo = latLng
                        mainViewModel.getRouting(clickablePositionOne!! to clickablePositionTwo!!)
                    } else {
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
                mainViewModel.uiState.routeDto?.let {
                    val polylinePoints = decodeRouteToLatLng(it)
                    Polyline(
                        points = polylinePoints,
                        color = Color.Blue,
                        width = 16f,
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

    @Composable
    fun ButtonPanel(modifier: Modifier, leg: Leg, onRoutingClicked: () -> Unit) {
        Column(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp)
                .zIndex(10f)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Start)
            ) {
                Text(text = leg.summary, modifier = Modifier.padding(8.dp))
                Row {
                    Text(
                        text = "${"%.1f".format(leg.distance / 1000)} ${stringResource(R.string.kilometer)}",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .heightIn(min = 16.dp, max = 20.dp)
                            .width(1.dp)
                            .background(color = Color.LightGray)
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = "${(leg.duration / 60).roundToInt()}  ${stringResource(R.string.minutes)}",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                }
            }

            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    onRoutingClicked()
                }) {
                Text(text = "Routing")
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