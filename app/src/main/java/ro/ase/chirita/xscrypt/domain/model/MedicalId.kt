package ro.ase.chirita.xscrypt.domain.model


data class MedicalId(
    var id: Int,
    var fullName: String,
    var dateOfBirth: String,
    var gender: String,
    var address: String,
    var city: String,
    var state: String,
    var phoneNumber: String,
    var weight: Int,
    var height: Int,
    var bloodGroup: String,
    var listOfAllergies: AllergyList,
    var listOfImmunizations: ImmunizationList,
    var listOfMedications: MedicationList

)
