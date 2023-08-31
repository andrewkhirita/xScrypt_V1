package ro.ase.chirita.xscrypt.presentation.sign_in

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.FragmentSigninBinding
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.presentation.MainActivity

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSigninBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureLoginButton()
        observeSignInResponse()
    }

    private fun configureLoginButton() {
        binding.btnLoginFragment.setOnClickListener {
            binding.btnLoginFragment.visibility = View.GONE
            binding.tvLoginTermsOfUse.visibility = View.GONE
            binding.progressBarSignIn.visibility = View.VISIBLE
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            viewModel.signIn(email, password)
        }
    }

    private fun observeSignInResponse() {
        viewModel.signInResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> handleSignInSuccess(response.data)
                is Response.Failure -> handleSignInFailure(response.e.message)
            }
        }
    }

    private fun handleSignInSuccess(data: Boolean) {
        if (data) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            requireActivity().finish()
            startActivity(intent)
        }
    }

    private fun handleSignInFailure(errorMessage: String?) {
        if (errorMessage != null && viewModel.showSnackbarOnFailure) {
            binding.btnLoginFragment.visibility = View.VISIBLE
            binding.tvLoginTermsOfUse.visibility = View.VISIBLE
            binding.progressBarSignIn.visibility = View.GONE
            val bgColor = ContextCompat.getColor(requireContext(), R.color.red)
            val textColor = ContextCompat.getColor(requireContext(), R.color.white)
            val snackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(bgColor)
            snackbar.setTextColor(textColor)
            snackbar.show()
            viewModel.showSnackbarOnFailure = false
        }
    }
}
