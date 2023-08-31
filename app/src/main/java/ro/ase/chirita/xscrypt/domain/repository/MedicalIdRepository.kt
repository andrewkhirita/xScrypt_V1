package ro.ase.chirita.xscrypt.domain.repository

import ro.ase.chirita.xscrypt.domain.model.MedicalId
import ro.ase.chirita.xscrypt.domain.model.Response

typealias CreateMedicalIdResponse = Response<Boolean>
typealias UpdateMedicalIdResponse = Response<Boolean>
typealias GetMedicalIdResponse = Response<MedicalId>
typealias DeleteMedicalIdResponse = Response<Boolean>
typealias DeleteAllergyPosition = Response<Boolean>
typealias DeleteImmunizationPosition = Response<Boolean>
typealias DeleteMedicationPosition = Response<Boolean>

interface MedicalIdRepository {

    suspend fun createMedicalId(medicalId: MedicalId): CreateMedicalIdResponse
    suspend fun updateMedicalId(medicalId: MedicalId): UpdateMedicalIdResponse
    suspend fun getMedicalId(): GetMedicalIdResponse
    suspend fun deleteMedicalId(): DeleteMedicalIdResponse
    suspend fun deleteAllergyPosition(parentPosition: Int, childPosition: Int): DeleteAllergyPosition
    suspend fun deleteImmunizationPosition(parentPosition: Int, childPosition: Int): DeleteImmunizationPosition
    suspend fun deleteMedicationPosition(parentPosition: Int, childPosition: Int): DeleteMedicationPosition

}