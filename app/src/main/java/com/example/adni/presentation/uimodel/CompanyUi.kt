package com.example.adni.presentation.uimodel

import com.example.adni.domain.model.Company
import com.example.adni.domain.model.Coordinates

data class CompanyUi(
        var id: Int,
        var name: String,
        var phone: String,
        var address: String,
        var email: String,
        var logoPath: String,
        var latitude: Double,
        var longitude: Double
)

fun CompanyUi.toCompanyModel() = Company(id, name, phone, address, email, logoPath,
        Coordinates(latitude, longitude))