package ro.ase.chirita.xscrypt.presentation.choose_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elrond.erdkotlin.domain.account.GetAccountUsecase
import com.elrond.erdkotlin.domain.networkconfig.GetNetworkConfigUsecase
import com.elrond.erdkotlin.domain.transaction.EstimateCostOfTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ase.chirita.xscrypt.core.Exceptions
import ro.ase.chirita.xscrypt.domain.model.Doctor
import ro.ase.chirita.xscrypt.domain.model.MedicalId
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.repository.MedicalIdRepository
import ro.ase.chirita.xscrypt.domain.repository.UserRepository
import ro.ase.chirita.xscrypt.domain.repository.WalletRepository
import javax.inject.Inject

@HiltViewModel
class ChooseDoctorViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val medicalIdRepository: MedicalIdRepository
): ViewModel() {

    private val _getDoctorsResponse = MutableLiveData<Response<List<Doctor>>>()
    val getDoctorsResponse: LiveData<Response<List<Doctor>>> = _getDoctorsResponse

    private val _getMedicalIdResponse = MutableLiveData<Response<MedicalId>>()
    val getMedicalIdResponse: LiveData<Response<MedicalId>> = _getMedicalIdResponse

    fun getDoctors(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = userRepository.getDoctors()
                when (response) {
                    is Response.Success -> {
                        _getDoctorsResponse.postValue(Response.Success(response.data))
                    }
                    is Response.Failure -> {
                        _getDoctorsResponse.postValue(Response.Failure(Exception(Exceptions.GET_LIST_DOCTORS_EXCEPTION)))
                    }
                }
            } catch (e: Exception) {
                _getDoctorsResponse.postValue(Response.Failure(e))
            }
        }
    }

    fun getMedicalId() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = medicalIdRepository.getMedicalId()
                when(success) {
                    is Response.Success -> {
                        _getMedicalIdResponse.postValue(Response.Success(success.data))
                    }
                    is Response.Failure -> {
                        _getMedicalIdResponse.postValue(Response.Failure(Exception(Exceptions.GET_MEDICAL_ID_EXCEPTION)))
                    }
                }
            } catch (e: Exception) {
                _getMedicalIdResponse.postValue(Response.Failure(e))
            }
        }
    }
}