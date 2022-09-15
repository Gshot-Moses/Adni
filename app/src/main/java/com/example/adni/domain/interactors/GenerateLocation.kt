package com.example.adni.domain.interactors

import android.location.LocationListener
import com.example.adni.domain.ILocationService
import javax.inject.Inject

class GenerateLocation @Inject constructor(private val locationService: ILocationService) {
    suspend fun provideLocation(listener: LocationListener) {
        return locationService.getCurrentLocation(listener)
    }

    fun stopService(listener: LocationListener) {
        locationService.stopListening(listener)
    }
}