package com.project.fat.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.project.fat.data.permission.PERMISSIONS
import com.project.fat.data.permission.PERMISSION_FLAG

object LocationProvider {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    fun init(activity : Context, fusedLocationProviderClient: FusedLocationProviderClient, locationCallback: LocationCallback){
        this.fusedLocationProviderClient = fusedLocationProviderClient
        this.locationCallback = locationCallback

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100).apply {
            setWaitForAccurateLocation(true)
            setMaxUpdateDelayMillis(500)
            setMinUpdateIntervalMillis(100)
        }.build()

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity as Activity, PERMISSIONS, PERMISSION_FLAG)
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}