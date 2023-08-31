package ro.ase.chirita.xscrypt.presentation.medical_id

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ase.chirita.xscrypt.core.Exceptions.DELETE_MEDICAL_ID_EXCEPTION
import ro.ase.chirita.xscrypt.core.Exceptions.GET_MEDICAL_ID_EXCEPTION
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.MedicalId
import ro.ase.chirita.xscrypt.domain.repository.MedicalIdRepository
import javax.inject.Inject

@HiltViewModel
class MedicalIdViewModel @Inject constructor(
    private val repo: MedicalIdRepository
):ViewModel() {

    private val _getMedicalIdResponse = MutableLiveData<Response<MedicalId>>()
    val getMedicalIdResponse: LiveData<Response<MedicalId>> = _getMedicalIdResponse

    private val _deleteMedicalIdResponse = MutableLiveData<Response<Boolean>>()
    val deleteMedicalIdResponse: LiveData<Response<Boolean>> = _deleteMedicalIdResponse

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

    fun deleteMedicalId(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = repo.deleteMedicalId()
                when(success) {
                    is Response.Success -> {
                        _deleteMedicalIdResponse.postValue(Response.Success(true))
                    }
                    is Response.Failure -> {
                        _deleteMedicalIdResponse.postValue(Response.Failure(Exception(
                            DELETE_MEDICAL_ID_EXCEPTION)))
                    }
                }
            } catch (e: Exception) {
                _deleteMedicalIdResponse.postValue(Response.Failure(e))
            }
        }
    }
}