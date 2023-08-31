package ro.ase.chirita.xscrypt.core

import android.content.Context
import android.text.format.DateUtils
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import ro.ase.chirita.xscrypt.core.Constants.NAME_PATTERN
import ro.ase.chirita.xscrypt.core.Constants.PHONE_NUMBER_PATTERN
import ro.ase.chirita.xscrypt.domain.model.MedicalId
import java.io.*
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

fun getRelativeTimeSpanString(inputDate: String): CharSequence {
    val dateTime = LocalDateTime.parse(inputDate, DateTimeFormatter.ISO_DATE_TIME)
    val timestamp = dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    val now = System.currentTimeMillis()
    return DateUtils.getRelativeTimeSpanString(
        timestamp,
        now,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    )
}

fun validateName(name: String) {
    if (!NAME_PATTERN.toRegex().matches(name)) {
        throw IllegalArgumentException("Name must be in the format e.g Ion Popescu")
    }
}

fun validateAddress(address: String) {
    if (address.length < 5) {
        throw IllegalArgumentException("Address must have minimum 5 characters")
    }
}

fun validateCity(city: String) {
    if (city.length < 3) {
        throw IllegalArgumentException("City must have minimum 3 characters")
    }
}

fun validateState(state: String) {
    if (state.length < 3) {
        throw IllegalArgumentException("State must have minimum 3 characters")
    }
}

fun validatePhoneNumber(phoneNumber: String) {
    val regex = Regex("^\\d{5,}\$")
    if (!regex.matches(phoneNumber)) {
        throw IllegalArgumentException("Phone number must contain only digits and have at least 5 digits")
    }
}

fun validateRecord(record: String){
    if (record.length < 3) {
        throw IllegalArgumentException("Record must have min. 3 characters")
    }
}

fun writeMedicalIdToJsonFile(medicalId: MedicalId, context: Context){
    val json = Gson().toJson(medicalId)
    val filename = "medicalId.json"
    val outputStream: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
    outputStream.write(json.toByteArray())
    outputStream.close()
}

fun readEncryptedJsonFromFile(context: Context): String? {
    val filename = "medicalId.json"
    val file = File(context.filesDir, filename)
    encryptJsonFile(file)

    if (!file.exists()) {
        ShowToast.show(context, "File doesn't exist!")
        return null
    }

    return try {
        val encryptedFile = File(file.parent, "${file.nameWithoutExtension}_encrypted.json")
        val encryptedBytes = encryptedFile.readBytes()
        Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    } catch (e: IOException) {
        ShowToast.show(context, "Error!")
        null
    }
}

 fun encryptJsonFile(jsonFile: File) {
    // Generează o cheie secretă AES
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(256)
    val secretKey: Key = keyGenerator.generateKey()

    // Criptează fișierul JSON
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encryptedJson = cipher.doFinal(FileInputStream(jsonFile).readBytes())

    // Salvează fișierul criptat
    val encryptedFile = File(jsonFile.parent, "${jsonFile.nameWithoutExtension}_encrypted.json")
    val outputStream = FileOutputStream(encryptedFile)
    outputStream.write(encryptedJson)
    outputStream.close()

    // Salvează cheia secretă
    val keyFile = File(jsonFile.parent, "${jsonFile.nameWithoutExtension}.key")
    val keyBytes = secretKey.encoded
    val keyOutputStream = FileOutputStream(keyFile)
    keyOutputStream.write(keyBytes)
    keyOutputStream.close()
}


fun readJsonFromFile(context: Context): String? {
    val filename = "medicalId.json"
    val file = File(context.filesDir, filename)
    encryptJsonFile(file)

    if (!file.exists()) {
        ShowToast.show(context,"File doesn't exist!")
    }

    return try {
        file.readText()
    } catch (e: IOException) {
        ShowToast.show(context,"Error!")
        null
    }
}

 fun readPrivateKeyFromFile(context: Context): String? {
    val filename = "medicalId.key"
    val keyFile = File(context.filesDir, filename)
    if (!keyFile.exists()) {
        ShowToast.show(context, "Key file doesn't exist!")
        return null
    }

    return try {
        val keyBytes = keyFile.readBytes()
        String(keyBytes)
    } catch (e: IOException) {
        ShowToast.show(context, "Error reading private key!")
        null
    }
}




