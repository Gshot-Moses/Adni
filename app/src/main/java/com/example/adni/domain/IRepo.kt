package com.example.adni.domain

import com.example.adni.domain.model.Company
import kotlinx.coroutines.flow.Flow

interface IRepo {
    suspend fun fetchAllCompanies(): Flow<List<Company>>
    //suspend fun insertAllCompanies(companies: List<CompanyEntity>)
    suspend fun insertCompany(company: Company): Int
    suspend fun getCompany(id: Int): Company
    suspend fun deleteCompany(id: Int): Int
    suspend fun searchCompany(query: String): List<Company>
    suspend fun updateCompanyLocation(latitude: Double, longitude: Double, id: Int)
    suspend fun updateCompanyPhone(phone: String, id: Int)
    suspend fun updateCompanyEmail(email: String, id: Int)
    suspend fun updateCompanyAddress(address: String, id: Int)
    suspend fun companyNameValidation(name: String): Boolean
    suspend fun companyLocationValidation(latitude: Double, longitude: Double): Boolean
}