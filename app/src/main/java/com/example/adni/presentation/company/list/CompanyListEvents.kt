package com.example.adni.presentation.company.list

import com.example.adni.presentation.uimodel.CompanyUi

sealed class CompanyListEvents {
    object OnData: CompanyListEvents()
    class StopSearch(val data: List<CompanyUi>): CompanyListEvents()
    class RemoveCompany(val id: Int): CompanyListEvents()
    class Search(val query: String): CompanyListEvents()
}
