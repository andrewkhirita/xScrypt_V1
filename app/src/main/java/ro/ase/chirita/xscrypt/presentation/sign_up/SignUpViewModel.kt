package ro.ase.chirita.xscrypt.presentation.sign_up

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ro.ase.chirita.xscrypt.core.Constants.PASSWORD_PATTERN
import ro.ase.chirita.xscrypt.core.Exceptions.EMAIL_PATTERN_EXCEPTION
import ro.ase.chirita.xscrypt.core.Exceptions.PASSWORD_EXCEPTION
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.User
import ro.ase.chirita.xscrypt.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: UserRepository
): ViewModel() {

    private val _signUpResponse = MutableLiveData<Response<Boolean>>()
    val signUpResponse: LiveData<Response<Boolean>> = _signUpResponse
    var showSnackbarOnFailure = true


    fun signUp(user: User, password: String) {
        viewModelScope.launch {
            if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
                _signUpResponse.value =
                    Response.Failure(Exception(EMAIL_PATTERN_EXCEPTION))
                showSnackbarOnFailure = true
            } else if (!password.matches(PASSWORD_PATTERN.toRegex())) {
                _signUpResponse.value = Response.Failure(Exception(PASSWORD_EXCEPTION))
                showSnackbarOnFailure = true
            } else {
                when (repo.signUp(user, password)) {
                    is Response.Success -> {
                        _signUpResponse.value = Response.Success(true)
                        showSnackbarOnFailure = false
                    }
                    else -> {}
                }
            }
        }
    }

}