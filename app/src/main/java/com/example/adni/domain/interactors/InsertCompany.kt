package com.example.adni.domain.interactors

import com.example.adni.domain.IRepo
import com.example.adni.domain.model.Company
import com.example.adni.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InsertCompany @Inject
constructor(private val repo: IRepo) {
    suspend fun insert(company: Company): Result<Int> {
        return withContext(Dispatchers.IO) {
            if (repo.companyNameValidation(company.name)) {
                if (repo.companyLocationValidation(
                        company.coordinates.latitude,
                        company.coordinates.longitude
                    )
                ) {
                    val output = repo.insertCompany(company)
                    if (output > 0)
                        Result.Success(output)
                    else Result.Failure("Erreur")
                } else
                    Result.Failure("Ces coordonnes sont enregistrees au nom d'une autre entreprise")
            } else {
                Result.Failure("Une entreprise avec ce nom existe dans la BD")
            }
        }
    }
}