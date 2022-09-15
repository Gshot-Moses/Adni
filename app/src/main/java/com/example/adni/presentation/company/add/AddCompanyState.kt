package com.example.adni.presentation.company.add

import com.example.adni.presentation.uimodel.CompanyUi

data class AddCompanyState(
    var companyUi: CompanyUi? = null,
    var loadingPosition: Boolean = false,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var added: Int = 0,
    var error: String = ""
)