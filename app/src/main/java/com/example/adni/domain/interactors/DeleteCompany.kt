package com.example.adni.domain.interactors

import com.example.adni.domain.IRepo
import com.example.adni.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCompany @Inject constructor(private val repo: IRepo) {
    suspend operator fun invoke(id: Int): Result<Int> {
        val result = withContext(Dispatchers.IO) {
            repo.deleteCompany(id)
        }
        if (result > 0)
            return Result.Success(result)
        return Result.Failure("Erreur lors de la suppression")
    }
}