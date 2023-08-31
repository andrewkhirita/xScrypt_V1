package ro.ase.chirita.xscrypt.core

object Exceptions {

    const val EMAIL_PATTERN_EXCEPTION = "Invalid credentials or email incorrect"
    const val PASSWORD_EXCEPTION = "Minimum 8 characters, 1 uppercase letter, 1 lowercase letter and 1 number"
    const val CREATE_USER_EXCEPTION = "Failed to create user"
    const val GET_USER_EXCEPTION = "Failed to retrieve user"
    const val SIGN_IN_EXCEPTION = "Invalid credentials. Please try again!"

    const val GET_NEWS_MEDICAL_EXCEPTION = "The list of medical news could not be loaded"
    const val ADD_IMAGE_STORAGE_EXCEPTION = "Oops. Something went wrong"
    const val ADD_IMAGE_TO_FIRESTORE_EXCEPTION = "Oops. The file could not be uploaded"
    const val FETCH_MEDICAL_ARTICLES_EXCEPTION = "Failed to fetch articles"
    const val GET_LIST_DOCTORS_EXCEPTION = "Oops. Something went wrong"

    const val GET_MEDICAL_ID_EXCEPTION = "Oops. Something went wrong"
    const val CREATE_MEDICAL_ID_EXCEPTION = "Oops. Something went wrong"
    const val UPDATE_MEDICAL_ID_EXCEPTION = "Oops. Something went wrong"
    const val DELETE_MEDICAL_ID_EXCEPTION = "Oops. Something went wrong"
    const val DELETE_HEALTH_RECORD_EXCEPTION = "Oops. Something went wrong"
    const val CHOOSE_DOCTOR_EXCEPTION = "There's no Medical ID available for send"

}