package ro.ase.chirita.xscrypt.data.repository

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import ro.ase.chirita.xscrypt.core.Constants.EMAIL
import ro.ase.chirita.xscrypt.core.Constants.IMAGES
import ro.ase.chirita.xscrypt.core.Constants.NAME
import ro.ase.chirita.xscrypt.core.Constants.PHOTO_URL
import ro.ase.chirita.xscrypt.core.Constants.STATUS
import ro.ase.chirita.xscrypt.core.Constants.USERS_FIRESTORE_REFERENCE
import ro.ase.chirita.xscrypt.core.Constants.WALLET
import ro.ase.chirita.xscrypt.core.Exceptions.CREATE_USER_EXCEPTION
import ro.ase.chirita.xscrypt.domain.model.Doctor
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.User
import ro.ase.chirita.xscrypt.domain.repository.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    @Named(USERS_FIRESTORE_REFERENCE) private val usersRef: CollectionReference,
): UserRepository {
    override val currentUser get() = auth.currentUser

    override suspend fun signIn(email: String, password: String): SignInResponse {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun signUp(user: User, password: String): SignUpResponse {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
            createUserInFirestore(authResult, user)
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    private suspend fun createUserInFirestore(
        authResult: AuthResult,
        user: User
    ) {
        val uid = authResult.user?.uid ?: throw Exception(CREATE_USER_EXCEPTION)
        val userData = mapOf(
            NAME to user.name,
            EMAIL to user.email,
            WALLET to user.wallet,
            STATUS to user.status,
            PHOTO_URL to user.photoUrl
        )
        val firestore = FirebaseFirestore.getInstance()
        firestore.runTransaction { transaction ->
            val userRef = usersRef.document(uid)
            if (!transaction.get(userRef).exists()) {
                transaction.set(userRef, userData)
            }
            null
        }.await()
    }



    override suspend fun updateUserWallet(email: String, wallet: String) {
        val firestore = FirebaseFirestore.getInstance()
        val query = usersRef.whereEqualTo(EMAIL, email).limit(1)
        val snapshot = query.get().await()
        if (snapshot.documents.isNotEmpty()) {
            val document = snapshot.documents[0]
            val userData = mapOf(
                WALLET to wallet
            )
            firestore.runTransaction { transaction ->
                transaction.update(document.reference, userData)
                null
            }.await()
        } else {
            throw Exception("User with email $email not found")
        }
    }

    override suspend fun getCurrentUser(): GetCurrentUserResponse {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            try {
                val userDoc = usersRef.document(firebaseUser.uid).get().await()
                val name = userDoc.get("name")
                val email = userDoc.get("email")
                val wallet = userDoc.get("wallet")
                val status = userDoc.get("status")
                val photoUrl = userDoc.get("photoUrl")
                val user = User(name!! as String, email!! as String, wallet!! as String,
                    status!! as String, photoUrl!! as String
                )
                Response.Success(user)
            } catch (e: Exception) {
                Response.Failure(e)
            }
        } else {
            Response.Failure(Exception("User not authenticated"))
        }
    }

    override suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse {
        return try {
            val downloadUrl = storage.reference.child(IMAGES).child(currentUser!!.uid + ".jpg")
                .putFile(imageUri).await()
                .storage.downloadUrl.await()
            Response.Success(downloadUrl)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun addImageUrlToFirestore(photoUrl: Uri): AddImageUrlToFirestoreResponse {
        return try {
            val storageRef = FirebaseStorage.getInstance().getReference("images/${currentUser!!.uid + ".jpg"}")
            storageRef.putFile(photoUrl).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            val firestore = FirebaseFirestore.getInstance()
            val document = usersRef.document(currentUser!!.uid)
            val userData = mapOf(
                PHOTO_URL to downloadUrl
            )
            firestore.runTransaction { transaction ->
                transaction.update(document, userData)
                null
            }.await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getDoctors(): GetDoctorsResponse {
        return try {
            val doctorList = mutableListOf<Doctor>()
            val querySnapshot = usersRef.whereEqualTo("status", "doctor").get().await()
            for (document in querySnapshot.documents) {
                val name = document.getString("name") ?: ""
                val wallet = document.getString("wallet") ?: ""
                val photoUrl = document.getString("photoUrl") ?: ""
                doctorList.add(Doctor(name, wallet, photoUrl))
            }
            Response.Success(doctorList)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}
