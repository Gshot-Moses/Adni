package com.example.adni.presentation.list

import com.example.adni.data.FakeRepo
import com.example.adni.domain.IRepo
import com.example.adni.domain.interactors.DeleteCompany
import com.example.adni.domain.interactors.GetAllCompanies
import com.example.adni.domain.interactors.SearchCompanies
import com.example.adni.presentation.company.list.CompanyListEvents
import com.example.adni.presentation.company.list.CompanyListState
import com.example.adni.presentation.company.list.CompanyListViewModel
import kotlinx.coroutines.runBlocking
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CompanyListViewModelTest {

    lateinit var SUT: CompanyListViewModel
    lateinit var getAllCompanies: GetAllCompanies
    lateinit var deleteCompany: DeleteCompany
    lateinit var searchCompanies: SearchCompanies
    lateinit var fakeRepo: IRepo

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        fakeRepo = FakeRepo()
        getAllCompanies = GetAllCompanies(fakeRepo)
        deleteCompany = DeleteCompany(fakeRepo)
        searchCompanies = SearchCompanies(fakeRepo)
        SUT = CompanyListViewModel(getAllCompanies, deleteCompany, searchCompanies)
    }

    @Test
    fun verify_loading_state() = runBlocking {
        // when
        SUT.state.observeForever {  }
        //SUT.onEvent(CompanyListEvents.OnData)
        //val expectedState = CompanyListState.OnCompanyList(data = listOf(), error = "No company")
        val returnedState = SUT.state.value
        assert(returnedState is CompanyListState.Loading)

    }

    @Test
    fun verify_SUT_correctly_loads_data() = runBlocking {
        (fakeRepo as FakeRepo).returnCompanyList = true
        SUT.state.observeForever {  }
        //SUT.onEvent(CompanyListEvents.OnData)
        val state = SUT.state.value
        assert(state is CompanyListState.OnCompanyList)
    }
}