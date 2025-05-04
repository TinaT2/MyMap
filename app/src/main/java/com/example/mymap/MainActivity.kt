package com.example.mymap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymap.ui.theme.MyMapTheme
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    val accessToken =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImRlOGM1Mzk2ODBkMTE4MjU1ZGRjNjM1NWUxNmZkNWZlN2RmYjZkYzNhNzUyMDQzMTkzMDlkYjgyM2YyNzc2YTk2ZTA5NWM4OWY5YTQwZDcyIn0.eyJhdWQiOiIzMjI4MiIsImp0aSI6ImRlOGM1Mzk2ODBkMTE4MjU1ZGRjNjM1NWUxNmZkNWZlN2RmYjZkYzNhNzUyMDQzMTkzMDlkYjgyM2YyNzc2YTk2ZTA5NWM4OWY5YTQwZDcyIiwiaWF0IjoxNzQ2MjY0MzY3LCJuYmYiOjE3NDYyNjQzNjcsImV4cCI6MTc0ODg1NjM2Nywic3ViIjoiIiwic2NvcGVzIjpbImJhc2ljIl19.SUI9gVEGM4sKFmtojwf9xSZMK2uaKT-6B_gKqEOj4UgZny6aZIQaC2v7mTA5f9Fz62V4kE1rJ5fu4-rJH9LbcntgQ_gFXrdr1oBMJaeFCF6065wWbw5vLp_rVFjOFQvHEnyB5pvjG6PREEtD_VVlQbDCPzxYtDbBrrp5mQknk0CrCiUk5kaqbqbOaCwtXy_Ss3BHeQwRk2FUyfhCeH-EAb-abW_mFRDRra8WzVWxBvl3EH51L_t4_LPYve1Epflo2CDpGg44Jm-8Kg4ImZzXDVNIIyBHZMiUQxlT9cNFurQfrYlQki_8lKl-qkHEoglMcEhvMvUVJUB1a0-SSm6Tlw"

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
    fun MyMapRouteScreen() {
        val context = LocalContext.current
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(35.6892, 51.3890), 12f) // Tehran
        }

        val polylinePoints = remember { mutableStateListOf<LatLng>() }

        // Call route API and decode polyline
        LaunchedEffect(Unit) {
            val encoded = getMockMapIrPolyline() // Replace with real API call
            polylinePoints.clear()
            polylinePoints.addAll(decodePolyline(encoded))
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
//        Polyline(
//            points = polylinePoints,
//            color = Color.Blue,
//            width = 8f
//        )
            MapEffect { map ->
                // Set map type to none to remove default tiles
                map.mapType = GoogleMap.MAP_TYPE_NONE

                // Add Map.ir tile overlay
                map.addTileOverlay(
                    TileOverlayOptions().tileProvider(
                        MapirUrlTileProvider(accessToken)
                    )
                )
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