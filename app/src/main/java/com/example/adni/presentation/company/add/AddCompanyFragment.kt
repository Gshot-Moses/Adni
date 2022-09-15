package com.example.adni.presentation.company.add

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.adni.R
import com.example.adni.databinding.FragmentAddCompanyBinding
import com.example.adni.presentation.uimodel.CompanyUi
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddCompanyFragment: Fragment() {

    private var _binding: FragmentAddCompanyBinding? = null
    private val binding: FragmentAddCompanyBinding get() = _binding!!
    private val viewModel: AddCompanyViewModel by viewModels()
    private var latitude = 0.0
    private var longitude = 0.0
    private var logoImagePath: String? = null
    private var fileUri: Uri? = null
    private var current: Long = 0L

    val cameraCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it)  fileUri?.let { launchCropIntent(it) }  //launchCropIntent()
    }

    val cropImageCallback = registerForActivityResult(CropImageRequestContract()) {
        fileUri = it
        Glide.with(requireContext()).load(it).into(binding.companyLogoView)
    }

    val selectPicCallback = registerForActivityResult(ActivityResultContracts.GetContent()) {
        launchCropIntent(it)
    }

    val permissionCallback = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        Log.d("permission", "${it.values}")
        if (it.values.all { it == true }) {
            launchLocationEvent()
            //toggleVisibilityLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        binding.loadLocationBtn.setOnClickListener {
            if (handlePermission()) {
                permissionCallback.launch(
                    arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"))
            }
            else {
                launchLocationEvent()
                //toggleVisibilityLocation()
            }
        }
        showAdditionalFields()
        addCompany()
        //handleImageFromCamera()
    }

    private fun launchLocationEvent() {
        viewModel.onEvent(AddCompanyEvents.GeneratePosition)
        current = System.currentTimeMillis()
    }

    private fun handlePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun launchIntentToViewLocation(latitude: Double, longitude: Double) {
        binding.viewLocationBtn.setOnClickListener {
            //Toast.makeText(requireContext(), "btn clicked", Toast.LENGTH_SHORT).show()
            //val uri = Uri.parse("geo:$latitude, $longitude")
            val uri = Uri.parse("http://maps.google.com/maps?q=loc:$latitude,$longitude(Entreprise)")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(requireContext().packageManager) != null)
                startActivity(intent)
            else
                Toast.makeText(requireContext(), "Aucune application disponible pour visualiser les coordonnes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleVisibilityLocation() {
        binding.locationContainer.visibility = View.VISIBLE
        binding.viewLocationBtn.visibility = View.VISIBLE

        binding.cancelLocationBtn.setOnClickListener {
            confirmCancelDialog().show()
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) {
            updateUi(it)
        }
    }

    private fun updateUi(state: AddCompanyState) {
        Log.d("state", "$state")
        showLocationProgress(state.loadingPosition)
        state.latitude?.let { stateLatitude ->
            state.longitude?.let { stateLongitude ->
                //viewModel.onEvent(AddCompanyEvents.StopLocationStreaming)
                latitude = stateLatitude
                binding.latitude.text = "Latitude: ${stateLatitude}"
                binding.longitude.text = "Longitude: ${stateLongitude}"
                longitude = stateLongitude
                toggleVisibilityLocation()
                viewModel.onEvent(AddCompanyEvents.StopLocationStreaming)
                launchIntentToViewLocation(stateLatitude, stateLongitude)
            }
        }
        if (state.added > 0) {
            Toast.makeText(requireContext(), "Soumis avec success", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        if (state.error.isNotBlank()) {
            Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
            if (state.error.startsWith("Vous devriez activer", ignoreCase = true))
                viewModel.onEvent(AddCompanyEvents.StopLocationStreaming)
        }
    }

    private fun showLocationProgress(visibility: Boolean) {
        if (visibility) binding.locationProgress.visibility = View.VISIBLE
        else binding.locationProgress.visibility = View.INVISIBLE
    }

    private fun showAdditionalFields() {
//        binding.addInfoToggle.setOnClickListener {
//            if (binding.additionalFieldsContainer.visibility == View.GONE) {
//                binding.additionalFieldsContainer.visibility = View.VISIBLE
//                binding.addIv.setImageResource(R.drawable.ic_minimize)
//            }
//            else {
//                binding.additionalFieldsContainer.visibility = View.GONE
//                binding.addIv.setImageResource(R.drawable.ic_add)
//            }
//        }
        binding.companyLogo.setOnClickListener {
            choosePhotoDialog().show()
        }
    }

    private fun confirmCancelDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Voulez-vous supprimer les coordonnees actuelles")
            .setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Ok") { dialog, _ ->
                latitude = 0.0
                longitude = 0.0
                binding.locationContainer.visibility = View.GONE
                binding.viewLocationBtn.visibility = View.GONE
                dialog.dismiss()
            }.create()
    }

    private fun choosePhotoDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setItems(
                arrayOf("Prendre photo avec la camera", "Choisir une image en local")
            ) { dialog, which ->
                when(which) {
                    0 -> launchCameraIntent()
                    1 -> launchGalleryIntent()
                }
            }.create()
    }

    private fun createLogoDirectory(): File {
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            logoImagePath = absolutePath
        }
    }

    private fun launchGalleryIntent() {
        selectPicCallback.launch("image/*")
    }

    private fun launchCameraIntent() {
        val file = try {
            createLogoDirectory()
        } catch (e: IOException) { null }
        file?.let {
            val uri = FileProvider
                .getUriForFile(requireContext(), "com.example.adni.fileprovider", it)
            fileUri = uri
            cameraCallback.launch(uri)
        }
    }

    private fun launchCropIntent(uri: Uri) {
        cropImageCallback.launch(uri)
    }

//    private fun decodeReturnedImage() {
//        val targetWidth: Int = binding.companyLogoView.width
//        val targetHeight: Int = binding.companyLogoView.height
//
//        val bmOptions = BitmapFactory.Options().apply {
//            inJustDecodeBounds = true
//            BitmapFactory.decodeFile(logoImagePath, this)
//
//            val imgWidth: Int = outWidth
//            val imgHeight: Int = outHeight
//
//            val scaleFactor: Int = Math.max(1,
//                Math.min(imgWidth / targetWidth, imgHeight / targetHeight))
//
//            inJustDecodeBounds = false
//            inSampleSize = scaleFactor
//            inPurgeable = true
//        }
//    }

    private fun addCompany() {
        binding.saveCompanyBtn.setOnClickListener {
            if (latitude != 0.0 && longitude != 0.0) {
                val companyName = binding.companyName.editText!!.text.toString()
                if (companyName.isEmpty()) {
                    Toast.makeText(requireContext(), "Remplir le champs nom", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val company = CompanyUi(0, companyName, "notSet", "notSet", "notSet", "notSet",
                    latitude, longitude)
                val phone = binding.phone.editText!!.text.toString()
                val address = binding.address.editText!!.text.toString()
                val email = binding.email.editText!!.text.toString()
                if (phone.isNotEmpty())
                    company.phone = phone
                if (address.isNotEmpty())
                    company.address = address
                if (email.isNotEmpty())
                    company.email = email
                fileUri?.let { company.logoPath = it.toString() }
                viewModel.onEvent(AddCompanyEvents.AddCompany(company))
                //Log.d("latitude", "$company")
            }
            else
                Toast.makeText(requireContext(), "Generer position avant de soummettre", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionCallback.unregister()
        selectPicCallback.unregister()
        cameraCallback.unregister()
        _binding = null
    }
}