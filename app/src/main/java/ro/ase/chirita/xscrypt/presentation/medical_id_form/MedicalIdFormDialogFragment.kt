package ro.ase.chirita.xscrypt.presentation.medical_id_form

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.ipfs.api.IPFS
import io.ipfs.api.Multipart
import io.ipfs.api.NamedStreamable.FileWrapper
import io.ipfs.multiaddr.MultiAddress
import kotlinx.coroutines.*
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.core.*
import ro.ase.chirita.xscrypt.databinding.FragmentDialogMedicalIdFormBinding
import ro.ase.chirita.xscrypt.domain.model.*
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class MedicalIdFormDialogFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogMedicalIdFormBinding
    private val viewModel: MedicalIdFormViewModel by viewModels()
    private lateinit var medicalId: MedicalId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dialog_medical_id_form, container, false)
        binding = FragmentDialogMedicalIdFormBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMedicalIdFromDatabase()
        saveMedicalIdToDatabase()
        closeDialog()
    }

    private fun closeDialog(){
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun saveMedicalIdToDatabase() {
        binding.cvSave.setOnClickListener {
            val patient = createMedicalIdObject()
            if (::medicalId.isInitialized) {
                patient.id = medicalId.id
                updateMedicalId(patient)
            } else {
                createMedicalId(patient)
            }
        }
    }

    private fun updateMedicalId(medicalId: MedicalId) {
        medicalId.id = medicalId.id
        viewModel.updateMedicalId(medicalId)
        writeMedicalIdToJsonFile(medicalId,requireContext())
        viewModel.updateMedicalIdResponse.observe(viewLifecycleOwner) { response ->
            handleResponse(response, "Medical ID was successfully updated!")
        }
    }

    private fun createMedicalId(medicalId: MedicalId) {
        viewModel.createMedicalId(medicalId)
        writeMedicalIdToJsonFile(medicalId,requireContext())
        viewModel.createMedicalIdResponse.observe(viewLifecycleOwner) { response ->
            handleResponse(response, "Medical ID was sucessfully created!")
        }
    }


    private fun handleResponse(response: Response<Boolean>, successMessage: String) {
        when (response) {
            is Response.Success -> {
                if (response.data) {
                    dismiss()
                    ShowToast.show(requireContext(), successMessage)
                }
            }
            is Response.Failure -> {
                ShowToast.show(requireContext(), "${response.e.message}")
            }
        }
    }


    private fun createMedicalIdObject(): MedicalId {
        val name = binding.etPatientFormFullName.text.toString()
        val dateOfBirth = "${binding.datePicker1.dayOfMonth}/${binding.datePicker1.month +1}/${binding.datePicker1.year}"
        val gender = selectGender()
        val address = binding.etPatientFormAddress.text.toString()
        val city =binding.etPatientFormCity.text.toString()
        val state = binding.etPatientFormState.text.toString()
        val phoneNumber = binding.ccpPatient.selectedCountryCode + binding.etPatientFormPhoneNumber.text.toString()
        val weight = binding.nbUserDetailsWeight.value
        val height = binding.nbUserDetailsHeight.value
        val bloodGroup = selectBloodGroup()

        val listOfAllergies = if (::medicalId.isInitialized) medicalId.listOfAllergies else AllergyList(mutableListOf())
        val listOfImmunizations = if (::medicalId.isInitialized) medicalId.listOfImmunizations else ImmunizationList(mutableListOf())
        val listOfMedications = if (::medicalId.isInitialized) medicalId.listOfMedications else MedicationList(mutableListOf())

        return MedicalId(id,name,dateOfBirth,gender,address,city,state,phoneNumber,weight,height,bloodGroup,listOfAllergies,listOfImmunizations,listOfMedications)
    }


    private fun uploadMedicalIdFromDb(medicalId: MedicalId){
        binding.etPatientFormFullName.setText(medicalId.fullName)
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val date = LocalDate.parse(medicalId.dateOfBirth, formatter)
        binding.datePicker1.init(date.year,date.monthValue - 1,date.dayOfMonth, null)

        var selectedGender: RadioButton? = null
        when (medicalId.gender) {
            "male" -> {
                selectedGender = binding.rbPatientFormMale
            }
            "female" -> {
                selectedGender = binding.rbPatientFormFemale
            }
        }
        selectedGender?.isChecked = true

        binding.etPatientFormAddress.setText(medicalId.address)
        binding.etPatientFormCity.setText(medicalId.city)
        binding.etPatientFormState.setText(medicalId.state)

        val nonePrefixPhoneNumber = medicalId.phoneNumber.substring(2)
        binding.etPatientFormPhoneNumber.setText(nonePrefixPhoneNumber)

        binding.nbUserDetailsWeight.value = medicalId.weight
        binding.nbUserDetailsHeight.value = medicalId.height

        var selectedBloodGroup: RadioButton? = null
        when (medicalId.bloodGroup) {
            "A" -> {
                selectedBloodGroup = binding.rbPatientFormBloodGroupA
            }
            "B" -> {
                selectedBloodGroup = binding.rbPatientFormBloodGroupB
            }
            "AB" -> {
                selectedBloodGroup = binding.rbPatientFormBloodGroupAb
            }
            "0" -> {
                selectedBloodGroup = binding.rbPatientFormBloodGroup0
            }
        }
        selectedBloodGroup?.isChecked = true
    }

    private fun selectGender(): String {
        val gender =when(binding.rgPatientFormGender.checkedRadioButtonId) {
            R.id.rb_patient_form_male -> "male"
            R.id.rb_patient_form_female -> "female"
            else-> ""
        }
        return gender
    }

    private fun selectBloodGroup(): String {
        val gender =when(binding.rgPatientFormBloodGroup.checkedRadioButtonId) {
            R.id.rb_patient_form_blood_group_a -> "A"
            R.id.rb_patient_form_blood_group_b -> "B"
            R.id.rb_patient_form_blood_group_ab -> "AB"
            R.id.rb_patient_form_blood_group_0 -> "0"
            else-> ""
        }
        return gender
    }

    private fun getMedicalIdFromDatabase(){
        viewModel.getMedicalId()
        viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    medicalId = response.data
                    uploadMedicalIdFromDb(medicalId)
                }
                is Response.Failure -> {

                }
            }
        }
    }

}