package com.example.adni.presentation.company.add

import android.location.LocationListener
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adni.domain.interactors.GenerateLocation
import com.example.adni.domain.interactors.InsertCompany
import com.example.adni.domain.utils.Result
import com.example.adni.presentation.uimodel.CompanyUi
import com.example.adni.presentation.uimodel.toCompanyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class AddCompanyViewModel
@Inject constructor(private val insertCompany: InsertCompany, private val generateLocation: GenerateLocation)
    : ViewModel() {

    private val _state: MutableLiveData<AddCompanyState> = MutableLiveData(AddCompanyState())
    val state: LiveData<AddCompanyState> get() = _state
    private val listener: LocationListener = LocationListener { location ->
        Log.d("location", "Lat: ${location.latitude}, Long: ${location.longitude}")
        _state.value = _state.value!!.copy(latitude = location.latitude, longitude = location.longitude,
            loadingPosition = false, error = "")
    }

    fun onEvent(event: AddCompanyEvents) {
        when(event) {
            is AddCompanyEvents.GeneratePosition -> generatePosition()
            is AddCompanyEvents.StopLocationStreaming -> stopListening()
            is AddCompanyEvents.AddCompany -> addCompany(event.companyUi)
        }
    }

    private fun generatePosition() {
        viewModelScope.launch(Dispatchers.Main) {
            generateLocation.provideLocation(listener)
            _state.value = _state.value!!.copy(loadingPosition = true, error = "")
            launch {
                delay(4000)
                val latitude = _state.value!!.latitude
                if (latitude != null)
                    _state.value = _state.value!!.copy(loadingPosition = false, error = "")
                else {
                    val error = "Vous devriez activer vos donnees mobiles afin de permettre la localisation"
                    _state.value = _state.value!!.copy(loadingPosition = false, error = error)
                }
            }
        }
    }

    private fun stopListening() {
        generateLocation.stopService(listener)
    }

    private fun addCompany(companyUi: CompanyUi) {
        viewModelScope.launch(Dispatchers.Main) {
            //Log.d("latitude", "${companyUi.toCompanyModel()}")
            val result = insertCompany.insert(companyUi.toCompanyModel())
            when(result) {
                is Result.Success -> _state.value = _state.value!!.copy(added = result.data, error = "")
                is Result.Failure -> _state.value = _state.value!!.copy(error = result.error)
            }
        }
    }
}