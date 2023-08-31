package ro.ase.chirita.xscrypt.data.source.local.dao

import androidx.room.*
import ro.ase.chirita.xscrypt.data.source.local.entity.MedicalIdEntity
import ro.ase.chirita.xscrypt.domain.model.AllergyList
import ro.ase.chirita.xscrypt.domain.model.ImmunizationList
import ro.ase.chirita.xscrypt.domain.model.MedicationList


@Dao
interface MedicalIdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicalId: MedicalIdEntity)

    @Update
    suspend fun update(medicalId: MedicalIdEntity)

    @Query("DELETE FROM medicalIds")
    suspend fun delete()

    @Query("SELECT * FROM medicalIds LIMIT 1")
    suspend fun getMedicalId(): MedicalIdEntity

    @Query("UPDATE medicalIds SET listOfAllergies = :allergies")
    suspend fun updateAllergies(allergies: AllergyList)

    @Query("UPDATE medicalIds SET listOfImmunizations = :immunizations")
    suspend fun updateImmunizations(immunizations: ImmunizationList)

    @Query("UPDATE medicalIds SET listOfMedications = :medications")
    suspend fun updateMedications(medications: MedicationList)

}


