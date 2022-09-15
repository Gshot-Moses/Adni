package com.example.adni.presentation.company.detail

import android.location.LocationListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adni.domain.interactors.GenerateLocation
import com.example.adni.domain.interactors.GetCompany
import com.example.adni.domain.interactors.UpdateCompanyInfo
import com.example.adni.domain.model.toCompanyUi
import com.example.adni.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyDetailsViewModel @Inject
constructor(private val getCompany: GetCompany,
            private val location: GenerateLocation,
            private val updateCompanyInfo: UpdateCompanyInfo)
    : ViewModel() {

    private val _state: MutableLiveData<CompanyDetailsState> = MutableLiveData(CompanyDetailsState())
    val state: LiveData<CompanyDetailsState> get() = _state
    private val locationListener = LocationListener { location ->
        _state.value = _state.value!!.copy(latitude = location.latitude, longitude = location.longitude)
    }

    fun onEvent(event: CompanyDetailsEvents) {
        when(event) {
            is CompanyDetailsEvents.LoadCompany -> loadCompany()
            is CompanyDetailsEvents.LocationUpdate -> locationUpdate()
            is CompanyDetailsEvents.StopLocationUpdates -> stopLocationUpdate()
            is CompanyDetailsEvents.UpdateAddress -> updateAddress(event.address)
            is CompanyDetailsEvents.UpdateEmail -> updateEmail(event.email)
            is CompanyDetailsEvents.UpdatePhone -> updatePhone(event.phone)
            is CompanyDetailsEvents.UpdateLocation -> updateLocation(event.latitude, event.longitude)
        }
    }

    private fun loadCompany() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = getCompany(_state.value!!.id)
            _state.value = _state.value!!.copy(companyUi = (result as Result.Success).data.toCompanyUi())
        }
    }

    private fun locationUpdate() {
        viewModelScope.launch(Dispatchers.Main) {
            location.provideLocation(locationListener)
        }
    }

    private fun stopLocationUpdate() {
        location.stopService(locationListener)
    }

    private fun updateLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCompanyInfo.updateLocation(latitude, longitude, _state.value!!.id)
        }
    }

    private fun updatePhone(phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCompanyInfo.updatePhone(phone, _state.value!!.id)
        }
    }

    private fun updateEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCompanyInfo.updatePhone(email, _state.value!!.id)
        }
    }

    private fun updateAddress(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCompanyInfo.updatePhone(address, _state.value!!.id)
        }
    }

    fun setId(id: Int) {
        _state.value = _state.value!!.copy(id = id)
    }
}