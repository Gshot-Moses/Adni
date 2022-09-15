package com.example.adni.domain.interactors

import com.example.adni.domain.IRepo
import com.example.adni.domain.model.Company
import com.example.adni.domain.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllCompanies @Inject constructor(private val repo: IRepo) {

    suspend operator fun invoke(): Flow<Result<List<Company>>> {
        val flow = withContext(Dispatchers.IO) {
            repo.fetchAllCompanies()
                .map {
                    if (it.isNullOrEmpty()) Result.Failure<List<Company>>("No company")
                    else Result.Success(it)
                }
        }
        return flow
    }
//    operator fun invoke(): Flow<Result<List<Company>>> = flow {
//        emit(Result.Loading())
//        val result = withContext(Dispatchers.IO) {
//            repo.fetchAllCompanies()
//                .map {
//                    if (it.isNullOrEmpty()) Result.Failure<List<Company>>("No company")
//                    else Result.Success(it)
//                }
//        }
//        emit(result.first())
//    }
}