package com.example.adni.presentation.company.add

import com.example.adni.presentation.uimodel.CompanyUi

sealed class AddCompanyEvents {
    object GeneratePosition: AddCompanyEvents()
    object StopLocationStreaming: AddCompanyEvents()
    class AddCompany(val companyUi: CompanyUi): AddCompanyEvents()
}