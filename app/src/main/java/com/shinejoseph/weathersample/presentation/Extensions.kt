package com.shinejoseph.weathersample.presentation

import android.content.Context
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.requestGPSOn(request: ActivityResultLauncher<IntentSenderRequest>) {   //new api
    val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY     //GPS accuracy
    }

    val settingRequest = LocationSettingsRequest.Builder().run {
        addLocationRequest(locationRequest)
        build()
    }

    val settingsClient = LocationServices.getSettingsClient(requireContext())
    val task =
        settingsClient.checkLocationSettings(settingRequest)         //【fire and receive result】

    task.addOnFailureListener {                             //if GPS is not on currently
        val intentSender = (it as ResolvableApiException).resolution.intentSender
        val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()

        request.launch(intentSenderRequest)
    }
}

fun Fragment.isGPSOn(): Boolean {
    val locationManager =
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Double.toDateTimeString(format: String, zone: TimeZone? = null): String {
    val date = Date().apply { time = this@toDateTimeString.toLong() }
    return SimpleDateFormat(format, Locale.ENGLISH)
        .apply { zone?.let { timeZone = it } }
        .format(date)
}