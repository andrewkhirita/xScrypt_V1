package ro.ase.chirita.xscrypt.domain.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import ro.ase.chirita.xscrypt.domain.model.Doctor
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.User

typealias SignUpResponse = Response<Boolean>
typealias SignInResponse = Response<Boolean>
typealias GetCurrentUserResponse = Response<User>
typealias AddImageToStorageResponse = Response<Uri>
typealias AddImageUrlToFirestoreResponse = Response<Boolean>
typealias GetDoctorsResponse = Response<List<Doctor>>

interface UserRepository {
    val currentUser: FirebaseUser?

    suspend fun signUp(user: User, password: String): SignUpResponse

    suspend fun signIn(email: String, password: String): SignInResponse

    suspend fun updateUserWallet(email: String, wallet: String)

    suspend fun getCurrentUser():GetCurrentUserResponse

    suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse

    suspend fun addImageUrlToFirestore(downloadUrl: Uri): AddImageUrlToFirestoreResponse

    suspend fun getDoctors():GetDoctorsResponse

}