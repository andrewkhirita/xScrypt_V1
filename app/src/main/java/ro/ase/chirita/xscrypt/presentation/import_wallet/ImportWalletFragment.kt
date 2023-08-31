package ro.ase.chirita.xscrypt.presentation.import_wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.core.ShowToast
import ro.ase.chirita.xscrypt.databinding.FragmentImportWalletBinding
import ro.ase.chirita.xscrypt.domain.model.CreateWalletAction
import ro.ase.chirita.xscrypt.presentation.MainActivity
import ro.ase.chirita.xscrypt.presentation.wallet_account.WalletAccountActivity

@AndroidEntryPoint
class ImportWalletFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentImportWalletBinding
    private val viewModel: ImportWalletViewModel by viewModels()
    private var doctorWalletAddress: String = "wallet"
    private var ehrPatient: String = "ehr"
    private var doctorName: String = "doctor"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_import_wallet, container, false)
        binding = FragmentImportWalletBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateWalletAddress()
        if (arguments != null && requireArguments().containsKey("doctorWalletAddress") && requireArguments().containsKey("ehrPatient")) {
            getInfo()
        }
    }

    private fun getInfo(){
        doctorWalletAddress = arguments?.getString("doctorWalletAddress")!!
        doctorName = arguments?.getString("doctorName")!!
        ehrPatient = arguments?.getString("ehrPatient")!!
    }

    private fun updateWalletAddress() {
        binding.btnImportWallet.setOnClickListener {
            viewModel.importWallet(binding.etImportWalletSecretPhrase.text.toString())
            viewModel.viewAction.observe(this) { viewAction ->
                when (viewAction){
                    is CreateWalletAction.CloseScreen -> {
                        if (doctorWalletAddress.isNotEmpty() && ehrPatient.isNotEmpty()) {
                            val intent = Intent(requireActivity(), WalletAccountActivity::class.java)
                            intent.putExtra("doctorWalletAddress", doctorWalletAddress)
                            intent.putExtra("doctorName",doctorName)
                            intent.putExtra("ehrPatient", ehrPatient)
                            startActivity(intent)
                        } else {
                            val intent = Intent(requireActivity(), WalletAccountActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    is CreateWalletAction.InvalidMnemonic -> {
                        ShowToast.show(requireContext(),"Invalid seed phrase")
                    }
                }
            }
        }
    }

}