package com.example.adni.data

import com.example.adni.data.dao.CompanyDao
import com.example.adni.data.entity.*
import com.example.adni.domain.IRepo
import com.example.adni.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Repo @Inject constructor(private val dao: CompanyDao): IRepo {

    override suspend fun fetchAllCompanies(): Flow<List<Company>> {
        return dao.getCompanies().map { it.map { companyEntity ->  companyEntity.toCompanyModel() } }
    }

    override suspend fun insertCompany(company: Company): Int {
        return dao.insertCompany(company.toCompanyEntity()).toInt()
    }

    override suspend fun getCompany(id: Int): Company {
        return dao.getCompanyFromId(id).toCompanyModel()
    }

    override suspend fun deleteCompany(id: Int): Int {
        return dao.deleteCompany(id)
    }

    override suspend fun searchCompany(query: String): List<Company> {
        return dao.searchCompanies(query).map { it.toCompanyModel() }
    }

    override suspend fun updateCompanyLocation(latitude: Double, longitude: Double, id: Int) {
        dao.updateCompanyLocation(latitude.toString(), longitude.toString(), id)
    }

    override suspend fun updateCompanyPhone(phone: String, id: Int) {
        dao.updateCompanyPhone(phone, id)
    }

    override suspend fun updateCompanyEmail(email: String, id: Int) {
        dao.updateCompanyEmail(email, id)
    }

    override suspend fun updateCompanyAddress(address: String, id: Int) {
        dao.updateCompanyAddress(address, id)
    }

    override suspend fun companyNameValidation(name: String): Boolean {
        return dao.getCompanyFromName(name) == null
    }

    override suspend fun companyLocationValidation(latitude: Double, longitude: Double): Boolean {
        return dao.getCompanyFromLocation(latitude.toString(), longitude.toString()) == null
    }
}