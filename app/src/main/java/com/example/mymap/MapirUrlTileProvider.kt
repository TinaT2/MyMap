package com.example.mymap

import com.google.android.gms.maps.model.UrlTileProvider
import java.net.MalformedURLException
import java.net.URL
import java.util.Locale
import kotlin.math.pow

class MapirUrlTileProvider(private val apiKey: String) :
    UrlTileProvider(256, 256) {
    var OSGEO_WMS: String = "https://map.ir/shiveh?" +
            "service=WMS" +
            "&version=1.1.0" +
            "&EXCEPTIONS=application/vnd.ogc.se_inimage" +
            "&request=GetMap" +
            "&layers=Shiveh:Shiveh" +
            "&width=256" +
            "&height=256" +
            "&srs=EPSG:3857" +
            "&format=image/png" +
            "&bbox=%f,%f,%f,%f&x-api-key="
    private val MINX = 0
    private val MAXX = 1
    private val MINY = 2
    private val MAXY = 3
    private val TILE_ORIGIN = doubleArrayOf(-20037508.34789244, 20037508.34789244)
    override fun getTileUrl(x: Int, y: Int, z: Int): URL? {
        val bbox = getBoundingBox(x, y, z)
        val s = String.format(
            Locale.US,
            OSGEO_WMS,
            bbox[MINX],
            bbox[MINY],
            bbox[MAXX],
            bbox[MAXY]
        ) + apiKey
        val url: URL?
        try {
            url = URL(s)
        } catch (e: MalformedURLException) {
            throw AssertionError(e)
        }
        return url
    }

    private fun getBoundingBox(x: Int, y: Int, zoom: Int): Array<Double?> {
        val tileSize = (20037508.34789244 * 2) / 2.0.pow(zoom.toDouble())
        val bbox = arrayOfNulls<Double>(4)
        bbox[MINX] = TILE_ORIGIN[0] + x * tileSize
        bbox[MINY] = TILE_ORIGIN[1] - (y + 1) * tileSize
        bbox[MAXX] = TILE_ORIGIN[0] + (x + 1) * tileSize
        bbox[MAXY] = TILE_ORIGIN[1] - y * tileSize
        return bbox
    }
}
