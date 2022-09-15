package com.example.adni.presentation.company.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adni.domain.interactors.DeleteCompany
import com.example.adni.domain.interactors.GetAllCompanies
import com.example.adni.domain.interactors.InsertCompany
import com.example.adni.domain.interactors.SearchCompanies
import com.example.adni.domain.model.toCompanyUi
import com.example.adni.domain.utils.Result
import com.example.adni.presentation.uimodel.CompanyUi
import com.example.adni.presentation.uimodel.toCompanyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListViewModel @Inject
constructor(private val getAllCompanies: GetAllCompanies,
            private val deleteCompany: DeleteCompany,
            private val searchCompanies: SearchCompanies)
    : ViewModel() {

    private val _state: MutableLiveData<CompanyListState> = MutableLiveData()
    val state: LiveData<CompanyListState> get() = _state

    init {
        _state.value = CompanyListState.Loading
        onEvent(CompanyListEvents.OnData)
    }

    fun onEvent(event: CompanyListEvents) {
        when(event) {
            is CompanyListEvents.OnData -> loadData()
            is CompanyListEvents.RemoveCompany -> removeCompany(event.id)
            is CompanyListEvents.Search -> searchCompany(event.query)
            is CompanyListEvents.StopSearch -> stopSearch(event.data)
        }
    }

    private fun stopSearch(data: List<CompanyUi>) {
        // switch state back to normal
        _state.value = CompanyListState.OnCompanyList(data = data)
    }

    private fun searchCompany(query: String) {
        // search for items from data store matching the query
        viewModelScope.launch(Dispatchers.Main) {
            val result = searchCompanies(query)
            when(result) {
                is Result.Success ->
                    _state.value = CompanyListState.OnSearch(data = result.data.map { it.toCompanyUi() })
                is Result.Failure ->
                    _state.value = CompanyListState.OnSearch(error = result.error)
            }
        }
    }

    private fun removeCompany(id: Int) {
        // item to be removed from list sent as id to viewModel
        // check if has successfully been removed
        viewModelScope.launch(Dispatchers.Main) {
            val result = deleteCompany(id)
            when(result) {
                is Result.Success -> _state.value = CompanyListState.OnCompanyToBeRemoved(id = result.data)
            }
        }
    }

    private fun loadData() {
        // event to fetch for data from data store
        viewModelScope.launch(Dispatchers.Main) {
            getAllCompanies().collect {
                when(it) {
                    //is Result.Loading -> _state.value = CompanyListState.Loading
                    is Result.Success -> _state.value = CompanyListState.OnCompanyList(data = it.data.map { it.toCompanyUi() })
                    is Result.Failure -> _state.value = CompanyListState.OnCompanyList(error = it.error)
                }
            }
        }
    }
}