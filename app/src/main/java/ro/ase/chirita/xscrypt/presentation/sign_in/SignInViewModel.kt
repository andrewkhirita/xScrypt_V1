package ro.ase.chirita.xscrypt.presentation.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ro.ase.chirita.xscrypt.core.Exceptions.SIGN_IN_EXCEPTION
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    private val _signInResponse = MutableLiveData<Response<Boolean>>()
    val signInResponse: LiveData<Response<Boolean>> = _signInResponse
    var showSnackbarOnFailure = true

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            when (repo.signIn(email, password)) {
                is Response.Success -> {
                    _signInResponse.value = Response.Success(true)
                    showSnackbarOnFailure = false
                }
                is Response.Failure -> {
                    _signInResponse.value = Response.Failure(Exception(SIGN_IN_EXCEPTION))
                    showSnackbarOnFailure = true
                }
            }
        }
    }
}
