package ro.ase.chirita.xscrypt.presentation.medical_id

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.viewModels
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import dagger.hilt.android.AndroidEntryPoint
import net.glxn.qrgen.android.QRCode
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.core.Constants.STORAGE_CODE
import ro.ase.chirita.xscrypt.core.ShowToast
import ro.ase.chirita.xscrypt.core.readEncryptedJsonFromFile
import ro.ase.chirita.xscrypt.core.readJsonFromFile
import ro.ase.chirita.xscrypt.core.readPrivateKeyFromFile
import ro.ase.chirita.xscrypt.databinding.FragmentMedicalIdBinding
import ro.ase.chirita.xscrypt.domain.model.MedicalId
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.presentation.medical_id_form.MedicalIdFormDialogFragment
import java.io.File
import java.io.FileOutputStream

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MedicalIdFragment : Fragment() {

    private lateinit var binding: FragmentMedicalIdBinding
    private val viewModel: MedicalIdViewModel by viewModels()
    private lateinit var medicalId: MedicalId

    private lateinit var front_anim: AnimatorSet
    private lateinit var back_anim: AnimatorSet
    private var isFront =true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicalIdBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openMedicalIdForm()
        deleteMedicalId()
        getMedicalIdFromDatabase()
        editMedicalId()
        refresh()
        setupAnimationCard()
        generatePDFFile()
    }

    private fun openMedicalIdForm(){
        binding.clTopCreate.setOnClickListener {
            val dialogFragment = MedicalIdFormDialogFragment()
            dialogFragment.show(parentFragmentManager, "PatientFormDialogFragment")
        }
    }

    private fun editMedicalId(){
        binding.ibEdit.setOnClickListener {
            val dialogFragment = MedicalIdFormDialogFragment()
            dialogFragment.show(parentFragmentManager, "PatientFormDialogFragment")
        }
    }


    private fun deleteMedicalId() {
        binding.ibDelete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Delete")
            alertDialogBuilder.setMessage("Are you sure you want to delete this item?")
            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                viewModel.deleteMedicalId()
                viewModel.deleteMedicalIdResponse.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Response.Success -> {
                            if (response.data) {
                                binding.cvFront.visibility = View.GONE
                                binding.clEditDelete.visibility = View.GONE
                                binding.clTopCreate.visibility = View.VISIBLE
                                ShowToast.show(requireContext(), "Medical record has been deleted!")
                            }
                        }
                        is Response.Failure -> {
                            ShowToast.show(requireContext(), "${response.e.message}")
                        }
                    }
                }
            }
            alertDialogBuilder.setNegativeButton("No") { _, _ ->
                // Se va executa atunci când utilizatorul apasă butonul "Nu" din dialogul de confirmare
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }


    private fun getMedicalIdFromDatabase(){
        viewModel.getMedicalId()
        viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    medicalId = response.data
                    binding.clEditDelete.visibility = View.VISIBLE
                    binding.cvFront.visibility = View.VISIBLE
                    binding.tvName.text = response.data.fullName.uppercase()
                    setQrCode()
                }
                is Response.Failure -> {
                    binding.clTopCreate.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun setQrCode(){
        val key = readPrivateKeyFromFile(requireContext())
        val qr = QRCode.from(key)
            .withSize(250, 250)
            .withColor(0xFF000000.toInt(), 0xFF00000000.toInt()).bitmap()
        binding.ivQrCode.setImageBitmap(qr)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun generatePDFFile(){
        binding.ibExportPdf.setOnClickListener {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission,STORAGE_CODE)
                }else{
                    savePDF()
                }
            }else{
                savePDF()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePDF()
                }else{
                    ShowToast.show(requireContext(),"Permission denied!")
                }
            }
        }
    }

    private fun savePDF(){
        val mDoc = Document()
        val mFileName = "xPass_" + medicalId.fullName

        val mFilePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "$mFileName.pdf")

        try{
            val fileOutputStream = FileOutputStream(mFilePath)
            PdfWriter.getInstance(mDoc,fileOutputStream)
            mDoc.open()

            val name = "Full name: "+ medicalId.fullName
            mDoc.add(Paragraph(name))

            val dateOfBirth = "Date of birth: " + medicalId.dateOfBirth
            mDoc.add(Paragraph(dateOfBirth))

            val gender = "Gender: " + medicalId.gender
            mDoc.add(Paragraph(gender))

            val address = "Address: " + medicalId.address
            mDoc.add(Paragraph(address))

            val city = "City: " + medicalId.city
            mDoc.add(Paragraph(city))

            val nationality = "Nationality: " + medicalId.state
            mDoc.add(Paragraph(nationality))

            val phoneNumber = "Phone number: " + medicalId.phoneNumber
            mDoc.add(Paragraph(phoneNumber))

            val weight = "Weight: " + medicalId.weight
            mDoc.add(Paragraph(weight))

            val height = "Height: " + medicalId.height
            mDoc.add(Paragraph(height))

            val bloodGroup = "Blood group: " + medicalId.bloodGroup
            mDoc.add(Paragraph(bloodGroup))

            val allergies = "Allergies: " + medicalId.listOfAllergies.allergyList
            mDoc.add(Paragraph(allergies))

            val immunizations = "Immunizations: " + medicalId.listOfImmunizations.immunizationList
            mDoc.add(Paragraph(immunizations))

            val medications = "Medications: " + medicalId.listOfMedications.medicationList
            mDoc.add(Paragraph(medications))

            mDoc.close()
            fileOutputStream.close()

            ShowToast.show(requireContext(),"PDF File was succesfully created. Check Downloads section!")
        }catch(e: Exception){
            ShowToast.show(requireContext(),"Oops. Error. Check connection!")

        }

    }


    private fun refresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMedicalId()
            viewModel.getMedicalIdResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Success -> {
                        medicalId = response.data
                        binding.clEditDelete.visibility = View.VISIBLE
                        binding.clTopCreate.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is Response.Failure -> {
                        binding.clTopCreate.visibility = View.VISIBLE
                        binding.clEditDelete.visibility = View.GONE
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setupAnimationCard(){
        var scale = requireContext().resources.displayMetrics.density

        binding.cvFront.cameraDistance = 8000 * scale
        binding.cvBack.cameraDistance = 8000 * scale

        front_anim = AnimatorInflater.loadAnimator(requireContext(), R.anim.font_animator) as AnimatorSet
        back_anim = AnimatorInflater.loadAnimator(requireContext(), R.anim.back_animator) as AnimatorSet

        binding.cvFront.setOnClickListener{
            binding.cvBack.visibility = View.VISIBLE
            if(isFront)
            {
                front_anim.setTarget(binding.cvFront);
                back_anim.setTarget(binding.cvBack);
                front_anim.start()
                back_anim.start()
                isFront = false
            }
            else
            {
                front_anim.setTarget(binding.cvBack)
                back_anim.setTarget(binding.cvFront)
                back_anim.start()
                front_anim.start()
                isFront = true
            }
        }
    }

}