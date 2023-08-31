package ro.ase.chirita.xscrypt.presentation.health_records_form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.core.ShowToast
import ro.ase.chirita.xscrypt.core.validateRecord
import ro.ase.chirita.xscrypt.core.writeMedicalIdToJsonFile
import ro.ase.chirita.xscrypt.databinding.FragmentDialogHealthRecordsFormBinding
import ro.ase.chirita.xscrypt.domain.model.MedicalId
import ro.ase.chirita.xscrypt.domain.model.Response

@AndroidEntryPoint
class HealthRecordsFormDialogFragment: BottomSheetDialogFragment(){

    private lateinit var binding: FragmentDialogHealthRecordsFormBinding
    private val viewModel: HealthRecordsFormViewModel by viewModels()
    private lateinit var medicalId: MedicalId
    private lateinit var selectedCategory: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_health_records_form, container, false)
        binding = FragmentDialogHealthRecordsFormBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        getMedicalIdFromDatabase()
        selectedSpinnerCategories()
        addHealthRecords()
    }

    private fun setupSpinner(){
        val mList = arrayOf<String?>("Allergies", "Immunizations", "Medications")
        val mArrayAdapter = ArrayAdapter<Any?>(requireContext(), R.layout.spn_list_item, mList)
        mArrayAdapter.setDropDownViewResource(R.layout.spn_list_item)
        binding.spnCategories.adapter = mArrayAdapter
    }

    private fun selectedSpinnerCategories(){
        binding.spnCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun addHealthRecords() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val category = selectedCategory

            try {
                validateRecord(name)
            } catch (e: IllegalArgumentException) {
                ShowToast.show(requireContext(), e.message.toString())
                return@setOnClickListener
            }

            val successMessage = when (category) {
                "Allergies" -> addAllergy(name)
                "Immunizations" -> addImmunization(name)
                "Medications" -> addMedication(name)
                else -> return@setOnClickListener
            }

            viewModel.updateMedicalId(medicalId)
            writeMedicalIdToJsonFile(medicalId,requireContext())
            viewModel.updateMedicalIdResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        if (response.data) {
                            dismiss()
                            ShowToast.show(requireContext(),successMessage)
                        }
                    }
                    is Response.Failure -> {
                        ShowToast.show(requireContext(),"${response.e.message}")
                    }
                }
            }
        }
    }

    private fun addAllergy(name: String): String {
        medicalId.listOfAllergies.allergyList.add(name)
        return "Medical record was succesfully updated with allergy $name!"
    }

    private fun addImmunization(name: String): String {
        medicalId.listOfImmunizations.immunizationList.add(name)
        return "Medical record was succesfully updated with immunization $name!"
    }

    private fun addMedication(name: String): String {
        medicalId.listOfMedications.medicationList.add(name)
        return "Medical record was succesfully updated with medication $name!"
    }


    private fun getMedicalIdFromDatabase(){
        viewModel.getMedicalId()
        viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    medicalId = response.data
                }
                is Response.Failure -> {
                    ShowToast.show(requireContext(),"${response.e.message}")
                }
            }
        }
    }
}