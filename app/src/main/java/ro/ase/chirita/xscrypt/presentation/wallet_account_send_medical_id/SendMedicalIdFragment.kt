package ro.ase.chirita.xscrypt.presentation.wallet_account_send_medical_id

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.core.readEncryptedJsonFromFile
import ro.ase.chirita.xscrypt.databinding.FragmentWalletAccountDetailsBinding
import ro.ase.chirita.xscrypt.domain.model.SendMedicalIdState

@AndroidEntryPoint
class SendMedicalIdFragment : Fragment() {

    private lateinit var binding: FragmentWalletAccountDetailsBinding
    private val viewModel: SendMedicalIdViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletAccountDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().intent?.let { intent ->

            val (doctorWalletAddress, ehrPatient) = setupComponentsIntent(intent)
            sendEHR(doctorWalletAddress, ehrPatient)
        }
        viewModel.viewState.observe(requireActivity()) { viewState ->
            when (viewState) {
                is SendMedicalIdState.Content -> {
                    updateContent(viewState)
                }
                else -> {}
            }
        }
    }

    private fun setupComponentsIntent(intent: Intent): Pair<String?, String?> {
        val doctorWalletAddress = intent.getStringExtra("doctorWalletAddress")
        val doctorName = intent.getStringExtra("doctorName")
        val ehrPatient = intent.getStringExtra("ehrPatient")

        if (doctorName != "doctor" && ehrPatient != "ehr") {
            binding.btnSend.visibility = View.VISIBLE
            binding.tvTransferTo.visibility = View.VISIBLE
            binding.cvWalletDoctor.visibility = View.VISIBLE
            binding.tvWalletDoctorName.text = "Dr. $doctorName"
            binding.tvWalletDoctorAddress.text = doctorWalletAddress
        }
        return Pair(doctorWalletAddress, ehrPatient)
    }

    private fun sendEHR(doctorWalletAddress: String?, ehrPatient: String?) {
        binding.btnSend.setOnClickListener {
            binding.btnSend.visibility = View.GONE
            binding.tvTransferTo.visibility = View.GONE
            binding.cvWalletDoctor.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.sendTransaction(
                    toAddress = doctorWalletAddress!!,
                    message = readEncryptedJsonFromFile(requireActivity())
                )
                binding.progressBar.visibility = View.GONE
                val bgColor = ContextCompat.getColor(requireContext(), R.color.neon_v2)
                val textColor = ContextCompat.getColor(requireContext(), R.color.black)
                val snackbar = Snackbar.make(
                    binding.root,
                    "The xPass has been successfully sent. Please check transactions!",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.setBackgroundTint(bgColor)
                snackbar.setTextColor(textColor)
                snackbar.show()
            }, 5000)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateContent(viewState: SendMedicalIdState.Content) {
        TransitionManager.beginDelayedTransition(
            binding.tvWalletAddress.parent as ViewGroup,
            Slide(Gravity.BOTTOM)
        )
        binding.tvWalletAddress.text = viewState.account.address
        binding.tvWalletBalance.text = viewState.account.balance + " eGLD"
    }


    override fun onResume() {
        super.onResume()
        viewModel.getWalletAccount()
    }
}