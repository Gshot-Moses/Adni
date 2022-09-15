package com.example.adni.presentation.company.detail

sealed class CompanyDetailsEvents {
    object LoadCompany: CompanyDetailsEvents()
    object LocationUpdate: CompanyDetailsEvents()
    object StopLocationUpdates: CompanyDetailsEvents()
    class UpdateLocation(val latitude: Double, val longitude: Double): CompanyDetailsEvents()
    class UpdatePhone(val phone: String): CompanyDetailsEvents()
    class UpdateEmail(val email: String): CompanyDetailsEvents()
    class UpdateAddress(val address: String): CompanyDetailsEvents()
}
