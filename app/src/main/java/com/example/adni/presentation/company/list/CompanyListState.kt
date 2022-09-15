package com.example.adni.presentation.company.list

import com.example.adni.presentation.uimodel.CompanyUi

sealed class CompanyListState {
        object Loading: CompanyListState()
        class OnCompanyList(val data: List<CompanyUi> = listOf(), val error: String = ""): CompanyListState()
        class OnSearch(val data: List<CompanyUi> = listOf(), val error: String = ""): CompanyListState()
        class OnCompanyToBeRemoved(val id: Int = -1): CompanyListState()
        class OnInsertRemovedCompany(val id: Int = -1): CompanyListState()
        class OnRemoveCompanyFromListState(val id: Int = -1): CompanyListState()
}
