package com.example.adni.presentation.company.detail

import com.example.adni.presentation.uimodel.CompanyUi

data class CompanyDetailsState(
    val id: Int = 0,
    val companyUi: CompanyUi? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
