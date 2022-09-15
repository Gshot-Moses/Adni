package com.example.adni.data.dao

import androidx.room.*
import com.example.adni.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDao {

    @Query("SELECT * FROM company")
    fun getCompanies(): Flow<List<CompanyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompanies(companies: List<CompanyEntity>)

    @Insert
    fun insertCompany(companyEntity: CompanyEntity): Long

    @Query("SELECT * FROM company WHERE companyId=:id")
    fun getCompanyFromId(id: Int): CompanyEntity

    @Query("DELETE FROM company WHERE companyId=:companyId")
    fun deleteCompany(companyId: Int): Int

    @Query("SELECT * FROM company WHERE name LIKE '%' || :query || '%' ")
    fun searchCompanies(query: String): List<CompanyEntity>

    @Query("UPDATE company SET latitude=:latitude AND longitude=:longitude WHERE companyId=:id")
    fun updateCompanyLocation(latitude: String, longitude: String, id: Int)

    @Query("UPDATE company SET phone=:phone WHERE companyId=:id")
    fun updateCompanyPhone(phone: String, id: Int)

    @Query("UPDATE company SET email=:email WHERE companyId=:id")
    fun updateCompanyEmail(email: String, id: Int)

    @Query("UPDATE company SET address=:address WHERE companyId=:id")
    fun updateCompanyAddress(address: String, id: Int)

    @Query("SELECT * FROM company WHERE name=:name")
    fun getCompanyFromName(name: String): CompanyEntity

    @Query("SELECT * FROM company WHERE latitude=:latitude AND longitude=:longitude")
    fun getCompanyFromLocation(latitude: String, longitude: String): CompanyEntity
}