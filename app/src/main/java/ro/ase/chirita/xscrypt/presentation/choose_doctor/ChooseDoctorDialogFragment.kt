package ro.ase.chirita.xscrypt.presentation.choose_doctor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.components.DoctorAdapter
import ro.ase.chirita.xscrypt.core.Exceptions.CHOOSE_DOCTOR_EXCEPTION
import ro.ase.chirita.xscrypt.core.ShowToast
import ro.ase.chirita.xscrypt.databinding.FragmentChooseDoctorDialogBinding
import ro.ase.chirita.xscrypt.domain.model.Doctor
import ro.ase.chirita.xscrypt.domain.model.MedicalId
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.presentation.import_wallet.ImportWalletFragment
import java.io.FileOutputStream

@AndroidEntryPoint
class ChooseDoctorDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChooseDoctorDialogBinding
    private val viewModel: ChooseDoctorViewModel by viewModels()
    private var doctorList = listOf<Doctor>()
    private var filteredList = listOf<Doctor>()
    private lateinit var doctorAdapter: DoctorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseDoctorDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMedicalId()
        binding.progressBar.visibility = View.VISIBLE
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        binding.rvDoctors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.getDoctors()
        viewModel.getDoctorsResponse.observe(requireActivity()) { response ->
            when(response){
                is Response.Success -> {
                    doctorList = response.data
                    doctorAdapter = DoctorAdapter(doctorList) { doctor ->
                        viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
                            when (response) {
                                is Response.Success -> {
                                    val doctorWalletAddress = doctor.wallet
                                    val doctorName = doctor.name
                                    val ehrPatient = response.data.address
                                    val bundle = Bundle().apply {
                                        putString("doctorWalletAddress", doctorWalletAddress)
                                        putString("doctorName", doctorName)
                                        putString("ehrPatient", ehrPatient)
                                    }
                                    val dialogFragment = ImportWalletFragment()
                                    dialogFragment.arguments = bundle
                                    dialogFragment.show(parentFragmentManager, "ImportWalletFragment")
                                    dismiss()
                                }
                                is Response.Failure -> {
                                    ShowToast.show(requireContext(),CHOOSE_DOCTOR_EXCEPTION)
                                }
                            }
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.rvDoctors.adapter = doctorAdapter
                    binding.clDoctors.visibility = View.VISIBLE
                    searchDoctor()
                }
                is Response.Failure -> {
                    Toast.makeText(requireContext(), "${response.e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun searchDoctor() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDoctors(s.toString())
            }
        })
    }

    private fun filterDoctors(searchText: String) {
        filteredList = doctorList.filter { doctor ->
            doctor.name.contains(searchText, ignoreCase = true)
        }
        doctorAdapter.updateList(filteredList)
    }



}