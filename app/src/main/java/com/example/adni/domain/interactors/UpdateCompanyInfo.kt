package com.example.adni.domain.interactors

import com.example.adni.domain.IRepo
import javax.inject.Inject

class UpdateCompanyInfo @Inject constructor(private val repo: IRepo) {
    suspend fun updateLocation(latitude: Double, longitude: Double, id: Int) {
        repo.updateCompanyLocation(latitude, longitude, id)
    }
    suspend fun updatePhone(phone: String, id: Int) {
        repo.updateCompanyPhone(phone, id)
    }
    suspend fun updateEmail(email: String, id: Int) {
        repo.updateCompanyEmail(email, id)
    }
    suspend fun updateAddress(address: String, id: Int) {
        repo.updateCompanyAddress(address, id)
    }
}