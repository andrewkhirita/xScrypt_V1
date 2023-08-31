package ro.ase.chirita.xscrypt.presentation.home

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.components.NewsAdapter
import ro.ase.chirita.xscrypt.core.SuccessMessages.UPDATE_PHOTO_HOME_SUCCESS
import ro.ase.chirita.xscrypt.databinding.FragmentHomeBinding
import ro.ase.chirita.xscrypt.domain.model.MedicalArticle
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.model.User
import ro.ase.chirita.xscrypt.presentation.choose_doctor.ChooseDoctorDialogFragment
import ro.ase.chirita.xscrypt.presentation.import_wallet.ImportWalletFragment
import java.util.*

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var newsList = mutableListOf<MedicalArticle>()
    private var user: User? = null
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarHome.visibility = View.VISIBLE
        updateGreetingMessage()
        openGallery()
        setDetailsAboutUser()
        setupRecyclerView()
        openDialog()
        getPatientAddressWallet()
    }

    private fun updateGreetingMessage() {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greetingMessage = when (currentHour) {
            in 0..11 -> "Good Morning,"
            in 12..17 -> "Good Afternoon,"
            else -> "Good Evening,"
        }
        binding.tvHomeFragmentGreetingsText.text = greetingMessage
    }


    private fun openDialog() {
        binding.cvDoctors.setOnClickListener {
            val dialogFragment = ChooseDoctorDialogFragment()
            dialogFragment.show(parentFragmentManager,"ChooseDoctorFragment")
        }
    }

    private fun openGallery() {
        binding.ivHomeProfilePicture.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            binding.cvHomeProfilePicture.visibility = View.GONE
            binding.progressBarImageProfile.visibility = View.VISIBLE
            binding.llUserMessage.visibility = View.GONE
            viewModel.addImageToStorage(uri)
            viewModel.uploadImageToStorageResponse.observe(viewLifecycleOwner) { response ->
                when(response){
                    is Response.Success -> {
                        binding.progressBarHome.visibility = View.GONE
                        addImageToFirestore(uri)
                    }
                    is Response.Failure -> handleFailureResponse(response.e.message)
                }
            }
        }
    }

    private fun addImageToFirestore(uri: Uri?) {
        viewModel.addImageUrlToFirestore(uri!!)
        viewModel.uploadImageToFirestoreResponse.observe(viewLifecycleOwner) { responseFirestore ->
            when (responseFirestore) {
                is Response.Success ->  {
                    handleSuccessResponse()
                    binding.progressBarImageProfile.visibility = View.GONE
                    binding.cvHomeProfilePicture.visibility = View.VISIBLE
                    binding.llUserMessage.visibility = View.VISIBLE
                    updatePhoto()
                }
                is Response.Failure -> handleFailureResponse(responseFirestore.e.message)
            }
        }
    }

    private fun updatePhoto() {
        viewModel.getCurrentUser()
        if (viewModel.getCurrentUserResponse.hasActiveObservers()) {
            return
        }else{
            viewModel.getCurrentUserResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        user = response.data
                        loadGlidePhoto()
                    }
                    is Response.Failure -> handleFailureResponse(response.e.message)
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setDetailsAboutUser() {
        if (user == null) {
            viewModel.getCurrentUser()
            viewModel.getCurrentUserResponse.removeObservers(viewLifecycleOwner)
            viewModel.getCurrentUserResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        binding.progressBarHome.visibility = View.GONE
                        user = response.data
                        binding.tvHomeFragmentUserName.text = user?.name
                        visibleComponents()
                        loadGlidePhoto()
                    }
                    is Response.Failure -> handleFailureResponse(response.e.message)
                }
            }
        } else {
            binding.progressBarHome.visibility = View.GONE
            binding.tvHomeFragmentUserName.text = user?.name
            visibleComponents()
            loadGlidePhoto()
        }
    }

    private fun visibleComponents() {
        binding.cvHomeProfilePicture.visibility = View.VISIBLE
        binding.llUserMessage.visibility = View.VISIBLE
        binding.clTop.visibility = View.VISIBLE
        binding.clAddressExport.visibility = View.VISIBLE
        binding.tvNews.visibility = View.VISIBLE
        binding.rvNews.visibility = View.VISIBLE
    }

    private fun loadGlidePhoto() {
        Glide.with(this)
            .load(user?.photoUrl)
            .into(binding.ivHomeProfilePicture)
    }

    private fun getPatientAddressWallet(){
        binding.ibGetAddress.setOnClickListener {
            viewModel.copyAddress(user!!.wallet,requireContext())
            val bgColor = ContextCompat.getColor(requireContext(), R.color.neon_v2)
            val textColor = ContextCompat.getColor(requireContext(), R.color.black)
            val snackbar = Snackbar.make(binding.root, "Address copied", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(bgColor)
            snackbar.setTextColor(textColor)
            snackbar.show()
        }
    }

    private fun handleFailureResponse(errorMessage: String?) {
        if (errorMessage != null && viewModel.showSnackbarOnFailure) {
            val bgColor = ContextCompat.getColor(requireContext(), R.color.red)
            val textColor = ContextCompat.getColor(requireContext(), R.color.white)
            if(snackbar == null){
                snackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
                snackbar?.setBackgroundTint(bgColor)
                snackbar?.setTextColor(textColor)
                snackbar?.show()
            }else {
                snackbar?.setText(errorMessage)
            }
            viewModel.showSnackbarOnFailure = false

        }
    }

    private fun handleSuccessResponse(){
        if (!viewModel.showSnackbarOnFailure) {
            val bgColor = ContextCompat.getColor(requireContext(), R.color.neon_v2)
            val textColor = ContextCompat.getColor(requireContext(), R.color.black)
            if(snackbar == null){
                snackbar = Snackbar.make(binding.root, UPDATE_PHOTO_HOME_SUCCESS, Snackbar.LENGTH_SHORT)
                snackbar?.setBackgroundTint(bgColor)
                snackbar?.setTextColor(textColor)
                snackbar?.show()
            }
            viewModel.showSnackbarOnFailure = true
        }
    }

    private fun setupRecyclerView() {
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        if (newsList.isNotEmpty()) {
            val newsAdapter = NewsAdapter(newsList) { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            binding.rvNews.adapter = newsAdapter
        } else {
            viewModel.getMedicalNews()
            viewModel.getMedicalNewsResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        binding.progressBarHome.visibility = View.GONE
                        newsList.addAll(response.data)
                        val newsAdapter = NewsAdapter(newsList) { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                        binding.rvNews.adapter = newsAdapter
                    }
                    is Response.Failure -> handleFailureResponse(response.e.message)
                }
            }
        }
    }

}