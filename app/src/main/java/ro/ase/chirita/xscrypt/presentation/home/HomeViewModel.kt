package ro.ase.chirita.xscrypt.presentation.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ase.chirita.xscrypt.core.Exceptions
import ro.ase.chirita.xscrypt.core.Exceptions.ADD_IMAGE_STORAGE_EXCEPTION
import ro.ase.chirita.xscrypt.core.Exceptions.ADD_IMAGE_TO_FIRESTORE_EXCEPTION
import ro.ase.chirita.xscrypt.core.Exceptions.GET_NEWS_MEDICAL_EXCEPTION
import ro.ase.chirita.xscrypt.domain.model.MedicalArticle
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.User
import ro.ase.chirita.xscrypt.domain.repository.MedicalNewsRepository
import ro.ase.chirita.xscrypt.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: UserRepository,
    private val news: MedicalNewsRepository,
): ViewModel() {

    private val _uploadImageToStorageResponse = MutableLiveData<Response<Boolean>>()
    val uploadImageToStorageResponse: LiveData<Response<Boolean>> = _uploadImageToStorageResponse

    private val _uploadImageToFirestoreResponse = MutableLiveData<Response<Boolean>>()
    val uploadImageToFirestoreResponse: LiveData<Response<Boolean>> = _uploadImageToFirestoreResponse

    private val _getCurrentUserResponse = MutableLiveData<Response<User>>()
    val getCurrentUserResponse: LiveData<Response<User>> = _getCurrentUserResponse

    private val _getMedicalNewsResponse = MutableLiveData<Response<List<MedicalArticle>>>()
    val getMedicalNewsResponse: LiveData<Response<List<MedicalArticle>>> = _getMedicalNewsResponse

    var showSnackbarOnFailure = true

    fun getMedicalNews(){
        viewModelScope.launch {
            val response = news.getListOfMedicalArticles()
            when(response){
                is Response.Success -> {
                    _getMedicalNewsResponse.postValue(Response.Success(response.data))
                    showSnackbarOnFailure = false
                }
                is Response.Failure -> {
                    _getMedicalNewsResponse.postValue(Response.Failure(Exception(GET_NEWS_MEDICAL_EXCEPTION)))
                    showSnackbarOnFailure = true
                }
            }
        }
    }

    fun getCurrentUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repo.getCurrentUser()
                when (response) {
                    is Response.Success -> {
                        _getCurrentUserResponse.postValue(Response.Success(response.data))
                        showSnackbarOnFailure = false
                    }
                    is Response.Failure -> {
                        _getCurrentUserResponse.postValue(Response.Failure(Exception(Exceptions.GET_USER_EXCEPTION)))
                        showSnackbarOnFailure = true
                    }
                }
            } catch (e: Exception) {
                _getCurrentUserResponse.postValue(Response.Failure(e))
            }
        }
    }

    fun addImageToStorage(imageUri: Uri){
        viewModelScope.launch {
            val success = repo.addImageToFirebaseStorage(imageUri)
            when(success){
                is Response.Success -> {
                    _uploadImageToStorageResponse.postValue(Response.Success(true))
                    showSnackbarOnFailure = false
                }
                is Response.Failure -> {
                    _uploadImageToStorageResponse.postValue(Response.Failure(Exception(
                        ADD_IMAGE_STORAGE_EXCEPTION)))
                    showSnackbarOnFailure = true
                }
            }
        }
    }

    fun addImageUrlToFirestore(imageUri: Uri){
        viewModelScope.launch {
            val success = repo.addImageUrlToFirestore(imageUri)
            when(success){
                is Response.Success -> {
                    _uploadImageToFirestoreResponse.postValue(Response.Success(true))
                    showSnackbarOnFailure = false
                }
                is Response.Failure -> {
                    _uploadImageToFirestoreResponse.postValue(Response.Failure(Exception(
                        ADD_IMAGE_TO_FIRESTORE_EXCEPTION)))
                    showSnackbarOnFailure = true
                }
            }
        }
    }

    fun copyAddress(text: String, context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }

}