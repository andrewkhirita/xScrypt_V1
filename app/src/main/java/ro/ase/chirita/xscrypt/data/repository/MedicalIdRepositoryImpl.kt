package ro.ase.chirita.xscrypt.data.repository

import ro.ase.chirita.xscrypt.data.mapper.fromEntity
import ro.ase.chirita.xscrypt.data.mapper.toEntity
import ro.ase.chirita.xscrypt.data.source.MedicalIdDatabase
import ro.ase.chirita.xscrypt.domain.model.MedicalId
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.repository.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicalIdRepositoryImpl @Inject constructor(
    private val database: MedicalIdDatabase
): MedicalIdRepository {

    override suspend fun createMedicalId(medicalId: MedicalId): CreateMedicalIdResponse {
        return try {
            database.getMedicalIdDao().insert(medicalId.toEntity())
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)

        }
    }

    override suspend fun updateMedicalId(medicalId: MedicalId): UpdateMedicalIdResponse {
        return try{
            database.getMedicalIdDao().update(medicalId.toEntity())
            Response.Success(true)
        }catch(e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun getMedicalId(): GetMedicalIdResponse{
        return try{
            val patientEntity = database.getMedicalIdDao().getMedicalId()
            val patient = fromEntity(patientEntity)
            Response.Success(patient)
        }catch(e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun deleteMedicalId(): DeleteMedicalIdResponse {
        return try{
            database.getMedicalIdDao().delete()
            Response.Success(true)
        }catch(e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun deleteAllergyPosition(
        parentPosition: Int, childPosition: Int
    ): DeleteAllergyPosition {
        return try {
            val patientEntity = database.getMedicalIdDao().getMedicalId()
            val patient = fromEntity(patientEntity)
            val updatedAllergiesList = patient.listOfAllergies.allergyList.toMutableList()
            updatedAllergiesList.removeAt(childPosition)
            patient.listOfAllergies.allergyList = updatedAllergiesList
            database.getMedicalIdDao().updateAllergies(patient.listOfAllergies)
            Response.Success(true)
        } catch(e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun deleteImmunizationPosition(
        parentPosition: Int,
        childPosition: Int
    ): DeleteImmunizationPosition {
        return try {
            val patientEntity = database.getMedicalIdDao().getMedicalId()
            val patient = fromEntity(patientEntity)
            val updatedImmunizationList = patient.listOfImmunizations.immunizationList.toMutableList()
            updatedImmunizationList.removeAt(childPosition)
            patient.listOfImmunizations.immunizationList = updatedImmunizationList
            database.getMedicalIdDao().updateImmunizations(patient.listOfImmunizations)
            Response.Success(true)
        } catch(e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun deleteMedicationPosition(
        parentPosition: Int,
        childPosition: Int
    ): DeleteMedicationPosition {
        return try {
            val patientEntity = database.getMedicalIdDao().getMedicalId()
            val patient = fromEntity(patientEntity)
            val updatedMedicationList = patient.listOfMedications.medicationList.toMutableList()
            updatedMedicationList.removeAt(childPosition)
            patient.listOfMedications.medicationList = updatedMedicationList
            database.getMedicalIdDao().updateMedications(patient.listOfMedications)
            Response.Success(true)
        } catch(e: Exception){
            Response.Failure(e)
        }
    }
}

