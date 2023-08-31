package ro.ase.chirita.xscrypt.core

object Constants{

    //SignUp Fields
    const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    const val NAME_PATTERN = "^([a-zA-Z]{1,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{1,}\\s?([a-zA-Z]{1,})?)"
    const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"
    const val PHONE_NUMBER_PATTERN = "^\\d{5,}\$"

    //Firestore Collection
    const val USERS_FIRESTORE_REFERENCE = "users"

    //Create User (Firestore) Fields
    const val NAME = "name"
    const val EMAIL = "email"
    const val WALLET = "wallet"
    const val STATUS = "status"
    const val PHOTO_URL = "photoUrl"

    //Firebase Storage
    const val IMAGES = "images"

    //MEDICAL NEWS API KEY
    const val API_KEY = "fb00cf71f05046498c971b0df9a40cf6"

    //PDF
    const val STORAGE_CODE = 1001

}
