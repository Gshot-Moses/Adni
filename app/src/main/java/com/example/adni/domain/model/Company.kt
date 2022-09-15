package com.example.adni.domain.model

import com.example.adni.data.entity.CompanyEntity
import com.example.adni.presentation.uimodel.CompanyUi

data class Company(
        var id: Int,
        var name: String,
        var phone: String = "",
        var address: String = "",
        var email: String = "",
        var logoPath: String = "",
        var coordinates: Coordinates,
)

fun Company.toCompanyEntity() = CompanyEntity(id, name, phone, address, email, logoPath,
        coordinates.latitude.toString(),
        coordinates.longitude.toString())

fun Company.toCompanyUi(): CompanyUi {
    return CompanyUi(id = id, name = name, phone = phone, address = address, email = email, logoPath,
            coordinates.latitude, coordinates.longitude)
}
