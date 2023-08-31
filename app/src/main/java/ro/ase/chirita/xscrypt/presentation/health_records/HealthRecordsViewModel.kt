package ro.ase.chirita.xscrypt.presentation.health_records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ase.chirita.xscrypt.core.Exceptions.DELETE_HEALTH_RECORD_EXCEPTION
import ro.ase.chirita.xscrypt.core.Exceptions.GET_MEDICAL_ID_EXCEPTION
import ro.ase.chirita.xscrypt.domain.model.MedicalId
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.repository.MedicalIdRepository
import javax.inject.Inject

@HiltViewModel
class HealthRecordsViewModel @Inject constructor(
    private val repo: MedicalIdRepository
): ViewModel(){

    private val _getMedicalIdResponse = MutableLiveData<Response<MedicalId>>()
    val getMedicalIdResponse: LiveData<Response<MedicalId>> = _getMedicalIdResponse

    private val _deleteAllergyPositionResponse = MutableLiveData<Response<Boolean>>()
    val deleteAllergyPositionResponse: LiveData<Response<Boolean>> = _deleteAllergyPositionResponse

    private val _deleteImmunizationPositionResponse = MutableLiveData<Response<Boolean>>()
    val deleteImmunizationPositionResponse: LiveData<Response<Boolean>> = _deleteImmunizationPositionResponse

    private val _deleteMedicationPositionResponse = MutableLiveData<Response<Boolean>>()
    val deleteMedicationPositionResponse: LiveData<Response<Boolean>> = _deleteMedicationPositionResponse


    fun getMedicalId() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = repo.getMedicalId()
                when(success) {
                    is Response.Success -> {
                        _getMedicalIdResponse.postValue(Response.Success(success.data))
                    }
                    is Response.Failure -> {
                        _getMedicalIdResponse.postValue(Response.Failure(Exception(GET_MEDICAL_ID_EXCEPTION)))
                    }
                }
            } catch (e: Exception) {
                _getMedicalIdResponse.postValue(Response.Failure(e))
            }
        }
    }

    fun deleteAllergyPosition(parentPosition: Int, childPosition: Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = repo.deleteAllergyPosition(parentPosition,childPosition)
                when(success) {
                    is Response.Success -> {
                        _deleteAllergyPositionResponse.postValue(Response.Success(true))
                    }
                    is Response.Failure -> {
                        _deleteAllergyPositionResponse.postValue(Response.Failure(Exception(
                            DELETE_HEALTH_RECORD_EXCEPTION)))
                    }
                }
            } catch (e: Exception) {
                _deleteAllergyPositionResponse.postValue(Response.Failure(e))
            }
        }
    }

    fun deleteImmunizationPosition(parentPosition: Int, childPosition: Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = repo.deleteImmunizationPosition(parentPosition,childPosition)
                when(success) {
                    is Response.Success -> {
                        _deleteImmunizationPositionResponse.postValue(Response.Success(true))
                    }
                    is Response.Failure -> {
                        _deleteImmunizationPositionResponse.postValue(Response.Failure(Exception(DELETE_HEALTH_RECORD_EXCEPTION)))

                    }
                }
            } catch (e: Exception) {
                _deleteImmunizationPositionResponse.postValue(Response.Failure(e))
            }
        }
    }

    fun deleteMedicationPosition(parentPosition: Int, childPosition: Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = repo.deleteMedicationPosition(parentPosition,childPosition)
                when(success) {
                    is Response.Success -> {
                        _deleteMedicationPositionResponse.postValue(Response.Success(true))
                    }
                    is Response.Failure -> {
                        _deleteMedicationPositionResponse.postValue(Response.Failure(Exception(DELETE_HEALTH_RECORD_EXCEPTION)))
                    }
                }
            } catch (e: Exception) {
                _deleteMedicationPositionResponse.postValue(Response.Failure(e))
            }
        }
    }
}