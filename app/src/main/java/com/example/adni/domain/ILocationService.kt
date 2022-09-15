package com.example.adni.domain

import android.location.LocationListener
import com.example.adni.domain.model.Coordinates

interface ILocationService {
    suspend fun getCurrentLocation(listener: LocationListener)
    fun stopListening(listener: LocationListener)
}