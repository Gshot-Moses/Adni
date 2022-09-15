package com.example.adni.domain.interactors

import com.example.adni.domain.IRepo
import com.example.adni.domain.model.Company
import com.example.adni.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCompany @Inject constructor(private val repo: IRepo) {
    suspend operator fun invoke(id: Int): Result<Company> {
        val result = withContext(Dispatchers.IO) {
            repo.getCompany(id)
        }
        return Result.Success(result)
    }
}