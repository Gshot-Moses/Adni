package com.example.adni.domain.interactors

import com.example.adni.domain.IRepo
import com.example.adni.domain.model.Company
import com.example.adni.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchCompanies @Inject constructor(private val repo: IRepo) {
    suspend operator fun invoke(query: String): Result<List<Company>> {
        val result = withContext(Dispatchers.IO) {
            repo.searchCompany(query)
        }
        if (result.isNullOrEmpty())
            return Result.Failure<List<Company>>("Pas de correspondance")
        return Result.Success(result)
    }
}