package ro.ase.chirita.xscrypt.presentation.sign_up

import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.core.Constants.EMAIL_PATTERN
import ro.ase.chirita.xscrypt.core.Constants.NAME_PATTERN
import ro.ase.chirita.xscrypt.core.Constants.PASSWORD_PATTERN
import ro.ase.chirita.xscrypt.databinding.FragmentSignupBinding
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.User
import ro.ase.chirita.xscrypt.presentation.create_wallet.CreateWalletActivity

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var binding: FragmentSignupBinding
    private var iconVerified: Drawable? = null
    private var iconError: Drawable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iconVerified = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_signup_verified)
        iconError = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_signup_error)

        validateFullName()
        validateEmail()
        validatePassword()

        signUp()
    }

    private fun signUp() {
        binding.btnSignupFragment.setOnClickListener {
            binding.btnSignupFragment.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            val user: User = createUser()
            viewModel.signUp(user, binding.etSignupPassword.text.toString())
            observeSignUpResponse(user)
        }
    }

    private fun observeSignUpResponse(user: User) {
        viewModel.signUpResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> handleSignUpSuccess(response.data,user)
                is Response.Failure -> handleSignUpFailure(response.e.message)
            }
        }
    }

    private fun handleSignUpSuccess(data: Boolean, user: User) {
        if(data){
            openCreateWalletActivity(user)
        }
    }

    private fun handleSignUpFailure(errorMessage: String?) {
        if (errorMessage != null && viewModel.showSnackbarOnFailure) {
            binding.btnSignupFragment.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            val bgColor = ContextCompat.getColor(requireContext(), R.color.red)
            val textColor = ContextCompat.getColor(requireContext(), R.color.white)
            val snackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(bgColor)
            snackbar.setTextColor(textColor)
            snackbar.show()
            viewModel.showSnackbarOnFailure = false
        }
    }

    private fun openCreateWalletActivity(user: User) {
        val intent = Intent(context, CreateWalletActivity::class.java)
        intent.putExtra("email", user.email)
        requireActivity().finish()
        startActivity(intent)
    }


    private fun createUser(): User {
        val name = binding.etSignupFullName.text.toString()
        val email = binding.etSignupEmail.text.toString()
        val status = selectCategory()
        val photoUrl = "null"
        return User(name, email, "", status,photoUrl)
    }

    private fun selectCategory(): String {
        val status = when (binding.rgSignupCategory.checkedRadioButtonId) {
            R.id.rb_signup_category_patient -> "patient"
            R.id.rb_signup_category_doctor -> "doctor"
            else -> ""
        }
        return status
    }


    private fun validateFullName() {
        iconError!!.bounds = Rect(0, 0, iconError!!.intrinsicWidth, iconError!!.intrinsicHeight)
        iconVerified!!.bounds =
            Rect(0, 0, iconVerified!!.intrinsicWidth, iconVerified!!.intrinsicHeight)
        binding.etSignupFullName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().matches(NAME_PATTERN.toRegex())) {
                    binding.etSignupFullName.setError("", iconVerified!!)
                } else {
                    binding.etSignupFullName.setError("", iconError!!)
                }
            }
        })
    }

    private fun validateEmail() {
        iconError!!.bounds = Rect(0, 0, iconError!!.intrinsicWidth, iconError!!.intrinsicHeight)
        iconVerified!!.bounds =
            Rect(0, 0, iconVerified!!.intrinsicWidth, iconVerified!!.intrinsicHeight)
        binding.etSignupEmail.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().matches(EMAIL_PATTERN.toRegex())) {
                    binding.etSignupEmail.setError("", iconVerified!!)
                } else {
                    binding.etSignupEmail.setError("", iconError!!)
                }
            }
        })
    }

    private fun validatePassword() {
        iconError!!.bounds = Rect(0, 0, iconError!!.intrinsicWidth, iconError!!.intrinsicHeight)
        iconVerified!!.bounds =
            Rect(0, 0, iconVerified!!.intrinsicWidth, iconVerified!!.intrinsicHeight)
        binding.etSignupPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().matches(PASSWORD_PATTERN.toRegex())) {
                    binding.etSignupPassword.setError("", iconVerified!!)
                } else {
                    binding.etSignupPassword.setError("", iconError!!)
                }
            }
        })
    }
}

