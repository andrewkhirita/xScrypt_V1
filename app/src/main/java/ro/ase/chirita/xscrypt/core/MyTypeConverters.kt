package ro.ase.chirita.xscrypt.core

import androidx.room.TypeConverter
import com.google.gson.Gson
import ro.ase.chirita.xscrypt.domain.model.AllergyList
import ro.ase.chirita.xscrypt.domain.model.ImmunizationList
import ro.ase.chirita.xscrypt.domain.model.MedicationList

class MyTypeConverters {

    @TypeConverter
    fun fromAllergyToJSON(allergyList: AllergyList): String {
        return Gson().toJson(allergyList)
    }
    @TypeConverter
    fun fromJSONtoAllergy(json: String): AllergyList {
        return Gson().fromJson(json,AllergyList::class.java)
    }

    @TypeConverter
    fun fromImmunizationToGson(immunizationList: ImmunizationList):String {
        return Gson().toJson(immunizationList)
    }
    @TypeConverter
    fun fromJSONtoImmunization(json:String):ImmunizationList{
        return Gson().fromJson(json,ImmunizationList::class.java)
    }

    @TypeConverter
    fun fromMedicationToJSON(medicationList: MedicationList):String {
        return Gson().toJson(medicationList)
    }

    @TypeConverter
    fun fromJsonToMedication(json: String): MedicationList{
        return Gson().fromJson(json,MedicationList::class.java)
    }
}