package com.example.mymap

import android.content.Context
import android.os.Build
import android.util.Log
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.Route
import java.nio.charset.StandardCharsets


class NetworkUtils(context: Context) {

    val userAgent = userAgentString(context)

    fun getOkHttpClient(apiKey: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain? ->
                chain!!.proceed(
                    chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .header("MapIr-SDK", userAgent)
                        .header("x-api-key", apiKey)
                        .build()
                )
            })
            .authenticator(Authenticator { route: Route?, response: Response? ->
                Log.e(
                    "MAPIR",
                    "Mapir APIKEY Not provided or expired, Please visit https://corp.map.ir/registration/ to get new APIKEY or extend yours"
                )
                null
            })
            .build()
    }

    private fun userAgentString(context: Context): String {
        val appName = getApplicationName(context)
        val encodedAppName =
            if (StandardCharsets.US_ASCII.newEncoder().canEncode(appName)) appName else String(
                appName.toByteArray(),
                StandardCharsets.US_ASCII
            )
        return String.format(
            "Android/%s(%s)(%s)-MapSdk/%s-%s(%s)/%s-(%s)",
            Build.VERSION.SDK_INT,
            Build.VERSION.RELEASE,
            Build.CPU_ABI,
            "۶.۰.۰",  // Assuming "6.0.0" is the MAPIR_SDK_VERSION
            context.getPackageName(),
            encodedAppName,
            Build.BRAND,
            Build.MODEL
        )
    }

    private fun getApplicationName(context: Context): String {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString()
    }

}