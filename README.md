# 🗺️ MyMap – Jetpack Compose Polyline Routing

MyMap is a lightweight Android app built with Jetpack Compose and Google Maps. It displays route polylines based on encoded geometry from [Map.ir](https://map.ir/) API. Fully written in Kotlin with a clean architecture approach and beautiful map rendering using the latest Google Maps Compose library.

---

## ✨ Features

- Decode and draw route polylines using encoded geometry
- Google Maps integration via `maps-compose`
- State-driven UI with ViewModel + `mutableStateOf`
- Prevents multiple API calls on recomposition
- Supports multi-leg route rendering
- Clean, simple structure and reusable components

---

## 🧱 Tech Stack

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Google Maps Compose](https://github.com/googlemaps/android-maps-compose)
- [Maps Utils KTX](https://github.com/googlemaps/android-maps-utils)
- Kotlin + MVVM
- DI Hilt

---

📸 Preview

<img src="https://github.com/user-attachments/assets/a44c2aa2-d8f5-4261-a32d-5b0e33161de9" alt="Preview" width="300"/>




🧪 Example
```
@Stable
interface RoutUiState {
    val routeDto: RouteDto?
}

private class MutableRoutUiState : RoutUiState {
    override var routeDto: RouteDto? by mutableStateOf(null)
}
```
```
   uiState.routeDto?.let {
                    val polylinePoints = decodeRouteToLatLng(it)
                    Polyline(
                        points = polylinePoints,
                        color = Color.Blue,
                        width = 16f,
                    )
 }
```

🛣️ TODO

- [ ] Support multi-leg routes
- [ ] Add user location tracking
- [ ] Animate camera along the route
- [ ] Offline map caching


🙌 Contributing
Feel free to fork, improve, or file issues. Stars are welcome 🌟
