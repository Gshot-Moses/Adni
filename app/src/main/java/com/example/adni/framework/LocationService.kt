package com.example.adni.framework

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import com.example.adni.domain.ILocationService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationService @Inject constructor(@ApplicationContext val context: Context): ILocationService {

    lateinit var locationManager: LocationManager

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(listener: LocationListener) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10L, 500F, listener)
    }

    override fun stopListening(listener: LocationListener) {
        locationManager.removeUpdates(listener)
    }
}