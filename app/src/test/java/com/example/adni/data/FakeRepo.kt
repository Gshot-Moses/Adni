package com.example.adni.data

import com.example.adni.domain.IRepo
import com.example.adni.domain.model.Company
import com.example.adni.domain.model.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo: IRepo {
    var returnCompanyList = false

    override suspend fun fetchAllCompanies(): Flow<List<Company>> {
        if (returnCompanyList)
            return flowOf(listOf(
                Company(0, "name", "phone", "address", "email",
                    "logo", Coordinates(0.0, 0.0))))
        return flowOf(listOf())
    }

    override suspend fun insertCompany(company: Company): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getCompany(id: Int): Company {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCompany(id: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun searchCompany(query: String): List<Company> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCompanyLocation(latitude: Double, longitude: Double, id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCompanyPhone(phone: String, id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCompanyEmail(email: String, id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCompanyAddress(address: String, id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun companyNameValidation(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun companyLocationValidation(latitude: Double, longitude: Double): Boolean {
        TODO("Not yet implemented")
    }
}