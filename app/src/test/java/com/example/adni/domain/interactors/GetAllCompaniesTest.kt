package com.example.adni.domain.interactors

import com.example.adni.domain.IRepo
import com.example.adni.domain.model.Company
import com.example.adni.domain.model.Coordinates
import com.example.adni.domain.utils.Result
import com.example.adni.data.FakeRepo
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAllCompaniesTest {

    lateinit var SUT: GetAllCompanies
    lateinit var fakeRepo: IRepo

    @Before
    fun setup() {
        fakeRepo = FakeRepo()
        SUT = GetAllCompanies(fakeRepo)
    }

    @Test
    fun SUT_returns_failure_when_flow_empty() = runBlocking {
        (fakeRepo as FakeRepo).returnCompanyList = false
        val result = SUT()
        assert(result.toList().size == 2)
        assert(result.toList()[0] is Result.Loading)
        assert(result.toList()[1] is Result.Failure)
    }

    @Test
    fun SUT_returns_success_when_flow_not_empty() = runBlocking {
        (fakeRepo as FakeRepo).returnCompanyList = true
        val result = SUT()
        val expectedCompany = Company(0, "name", "phone", "address", "email",
            "logo", Coordinates(0.0, 0.0))
        val outcome = (result.toList()[0] as Result.Success).data
        assert(result.toList()[0] is Result.Success)
        assert(outcome[0] == expectedCompany)
    }
}