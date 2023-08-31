package ro.ase.chirita.xscrypt.presentation.health_records

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.components.AllergyListAdapter
import ro.ase.chirita.xscrypt.components.ImmunizationListAdapter
import ro.ase.chirita.xscrypt.components.MedicationListAdapter
import ro.ase.chirita.xscrypt.core.ShowToast
import ro.ase.chirita.xscrypt.core.SuccessMessages.DELETED_ITEM
import ro.ase.chirita.xscrypt.databinding.FragmentHealthRecordsBinding
import ro.ase.chirita.xscrypt.domain.model.*
import ro.ase.chirita.xscrypt.presentation.health_records_form.HealthRecordsFormDialogFragment

@AndroidEntryPoint
class HealthRecordsFragment : Fragment() {

    private lateinit var binding: FragmentHealthRecordsBinding
    private val viewModel: HealthRecordsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHealthRecordsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDeleteAllergyPositionResponse()
        observeDeleteImmunizationPositionResponse()
        observeDeleteMedicationPositionResponse()
        setupAllergyList()
        setupImmunizationList()
        setupMedicationList()
        setupHealthRecordsFormDialog()
        refresh()
    }

    private fun setupAllergyList() {
        viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    binding.clTopNoMedicalCard.visibility = View.GONE
                    binding.llAdd.visibility = View.VISIBLE
                    binding.rvAllergy.visibility = View.VISIBLE
                    val adapter = AllergyListAdapter(listOf(response.data.listOfAllergies) as MutableList<AllergyList>)  { parentPosition, childPosition ->
                        viewModel.deleteAllergyPosition(parentPosition, childPosition)
                    }
                    binding.rvAllergy.adapter = adapter
                }
                is Response.Failure -> {
                    binding.clTopNoMedicalCard.visibility = View.VISIBLE
                    binding.llAdd.visibility = View.GONE
                }
            }
        }
        viewModel.getMedicalId()
    }

    private fun setupImmunizationList() {
        viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    binding.clTopNoMedicalCard.visibility = View.GONE
                    binding.llAdd.visibility = View.VISIBLE
                    binding.rvImmunization.visibility = View.VISIBLE
                    val adapter = ImmunizationListAdapter(listOf(response.data.listOfImmunizations) as MutableList<ImmunizationList>)  { parentPosition, childPosition ->
                        viewModel.deleteImmunizationPosition(parentPosition, childPosition)
                    }
                    binding.rvImmunization.adapter = adapter
                }
                is Response.Failure -> {
                    binding.clTopNoMedicalCard.visibility = View.VISIBLE
                    binding.llAdd.visibility = View.GONE
                }
            }
        }
        viewModel.getMedicalId()
    }

    private fun setupMedicationList() {
        viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    binding.clTopNoMedicalCard.visibility = View.GONE
                    binding.llAdd.visibility = View.VISIBLE
                    binding.rvMedication.visibility = View.VISIBLE
                    val adapter = MedicationListAdapter(listOf(response.data.listOfMedications) as MutableList<MedicationList>)  { parentPosition, childPosition ->
                        viewModel.deleteMedicationPosition(parentPosition, childPosition)
                    }
                    binding.rvMedication.adapter = adapter
                }
                is Response.Failure -> {
                    binding.llAdd.visibility = View.GONE
                    binding.clTopNoMedicalCard.visibility = View.VISIBLE
                }
            }
        }
        viewModel.getMedicalId()
    }


    private fun observeDeleteAllergyPositionResponse() {
        viewModel.deleteAllergyPositionResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    if (response.data) {
                        viewModel.getMedicalId()
                        ShowToast.show(requireContext(),DELETED_ITEM)
                    }
                }
                is Response.Failure -> {
                    ShowToast.show(requireContext(),"${response.e.message}")
                }
            }
        }
    }

    private fun observeDeleteImmunizationPositionResponse() {
        viewModel.deleteImmunizationPositionResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    if (response.data) {
                        viewModel.getMedicalId()
                        ShowToast.show(requireContext(),DELETED_ITEM)
                    }
                }
                is Response.Failure -> {
                    ShowToast.show(requireContext(),"${response.e.message}")
                }
            }
        }
    }

    private fun observeDeleteMedicationPositionResponse() {
        viewModel.deleteMedicationPositionResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    if (response.data) {
                        viewModel.getMedicalId()
                        ShowToast.show(requireContext(),DELETED_ITEM)
                    }
                }
                is Response.Failure -> {
                    ShowToast.show(requireContext(),"${response.e.message}")
                }
            }
        }
    }

    private fun setupHealthRecordsFormDialog() {
        binding.ibAdd.setOnClickListener {
            val dialogFragment = HealthRecordsFormDialogFragment()
            dialogFragment.show(parentFragmentManager, "HealthRecordsFormDialogFragment")
        }
    }

    private fun refresh(){
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMedicalId()
            viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        binding.llAdd.visibility = View.VISIBLE
                        binding.rvAllergy.visibility = View.VISIBLE
                        binding.rvImmunization.visibility = View.VISIBLE
                        binding.rvMedication.visibility = View.VISIBLE
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is Response.Failure -> {
                        binding.llAdd.visibility = View.GONE
                        binding.rvAllergy.visibility = View.GONE
                        binding.rvImmunization.visibility = View.GONE
                        binding.rvMedication.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }
    }
}
