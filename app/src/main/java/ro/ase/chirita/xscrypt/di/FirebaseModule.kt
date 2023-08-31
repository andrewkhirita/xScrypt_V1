package ro.ase.chirita.xscrypt.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ro.ase.chirita.xscrypt.core.Constants.USERS_FIRESTORE_REFERENCE
import ro.ase.chirita.xscrypt.data.repository.UserRepositoryImpl
import ro.ase.chirita.xscrypt.domain.repository.UserRepository
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseStorage() =  Firebase.storage

    @Provides
    fun provideAuthRepository(@Named(USERS_FIRESTORE_REFERENCE) usersRef: CollectionReference,
                              storage: FirebaseStorage): UserRepository = UserRepositoryImpl(
        auth = Firebase.auth,
        usersRef = usersRef,
        storage = storage
    )

    @Provides
    @Named(USERS_FIRESTORE_REFERENCE)
    fun provideUsersRef(db: FirebaseFirestore) = db.collection(USERS_FIRESTORE_REFERENCE)

}