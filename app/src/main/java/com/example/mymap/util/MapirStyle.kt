package com.example.mymap.util

object MapirStyle {
    /**
     * Map.ir basic map style
     */
    @Deprecated("")
    val MAIN_MOBILE_VECTOR_STYLE = "https://map.ir/vector/styles/main/main_mobile_style.json"
    const val VERNA = "https://map.ir/vector/styles/main/main_mobile_style.json"
    /**
     * Map.ir dark map style
     */
    const val CARMANIA = "https://map.ir/vector/styles/main/mapir-style-dark.json"
    /**
     * Map.ir basic map style with minimum POIs
     */
    const val ISATIS = "https://map.ir/vector/styles/main/mapir-style-min-poi.json"
    /**
     * Map.ir basic map style with no POIs
     */
    const val LIGHT = "https://map.ir/vector/styles/main/mapir-xyz-style-min-poi.json"
    /**
     * Map.ir basic map style with world tiles
     */
    const val WORLD =
        "https://map.ir/vector/styles/main/mapir-xyz-style.json" // The HYRCANIA constant is commented out in the original Kotlin code, so it's not included here.
}