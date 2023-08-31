package ro.ase.chirita.xscrypt.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ase.chirita.xscrypt.core.Exceptions.GET_USER_EXCEPTION
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.User
import ro.ase.chirita.xscrypt.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: UserRepository
): ViewModel() {
    private val _getCurrentUserResponse = MutableLiveData<Response<User>>()
    val getCurrentUserResponse: LiveData<Response<User>> = _getCurrentUserResponse

    fun getCurrentUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repo.getCurrentUser()
                when (response) {
                    is Response.Success -> {
                        _getCurrentUserResponse.postValue(Response.Success(response.data))
                    }
                    is Response.Failure -> {
                        _getCurrentUserResponse.postValue(Response.Failure(Exception(GET_USER_EXCEPTION)))
                    }
                }
            } catch (e: Exception) {
                _getCurrentUserResponse.postValue(Response.Failure(e))
            }
        }
    }
}