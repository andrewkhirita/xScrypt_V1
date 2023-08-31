package ro.ase.chirita.xscrypt.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.ase.chirita.xscrypt.domain.model.AllergyList
import ro.ase.chirita.xscrypt.domain.model.ImmunizationList
import ro.ase.chirita.xscrypt.domain.model.MedicationList

@Entity(tableName = "medicalIds")
data class MedicalIdEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "fullName") var fullName: String,
    @ColumnInfo(name = "dateOfBirth") var dateOfBirth: String,
    @ColumnInfo(name = "gender") var gender: String,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "city") var city: String,
    @ColumnInfo(name = "state") var state: String,
    @ColumnInfo(name = "phoneNumber") var phoneNumber: String,
    @ColumnInfo(name = "weight") var weight: Int,
    @ColumnInfo(name = "height") var height: Int,
    @ColumnInfo(name = "bloodGroup") var bloodGroup: String,
    @ColumnInfo(name = "listOfAllergies") var listOfAllergies: AllergyList,
    @ColumnInfo(name = "listOfImmunizations") var listOfImmunizations: ImmunizationList,
    @ColumnInfo(name = "listOfMedications") var listOfMedications: MedicationList,
)

