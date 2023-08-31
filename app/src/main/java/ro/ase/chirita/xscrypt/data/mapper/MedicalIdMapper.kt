package ro.ase.chirita.xscrypt.data.mapper

import ro.ase.chirita.xscrypt.data.source.local.entity.MedicalIdEntity
import ro.ase.chirita.xscrypt.domain.model.MedicalId

fun MedicalId.toEntity() = MedicalIdEntity(
    id = id,
    fullName = fullName,
    dateOfBirth = dateOfBirth,
    gender = gender,
    address = address,
    city = city,
    state = state,
    phoneNumber = phoneNumber,
    weight = weight,
    height = height,
    bloodGroup = bloodGroup,
    listOfAllergies = listOfAllergies,
    listOfImmunizations = listOfImmunizations,
    listOfMedications = listOfMedications,
)

fun fromEntity(medicalIdEntity: MedicalIdEntity): MedicalId {
    return MedicalId(
        id = medicalIdEntity.id,
        fullName = medicalIdEntity.fullName,
        dateOfBirth = medicalIdEntity.dateOfBirth,
        gender = medicalIdEntity.gender,
        address = medicalIdEntity.address,
        city = medicalIdEntity.city,
        state = medicalIdEntity.state,
        phoneNumber = medicalIdEntity.phoneNumber,
        weight = medicalIdEntity.weight,
        height = medicalIdEntity.height,
        bloodGroup = medicalIdEntity.bloodGroup,
        listOfAllergies = medicalIdEntity.listOfAllergies,
        listOfImmunizations = medicalIdEntity.listOfImmunizations,
        listOfMedications = medicalIdEntity.listOfMedications
    )
}