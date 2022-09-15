package com.example.adni.presentation.company.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.adni.databinding.CustomEditTextViewBinding
import com.example.adni.databinding.FragmentCompanyDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanyDetailsFragment: Fragment() {

    private var _binding: FragmentCompanyDetailsBinding? = null
    private val binding: FragmentCompanyDetailsBinding get() = _binding!!
    private val viewModel: CompanyDetailsViewModel by viewModels()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var locationUpdateDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setId(requireArguments()["id"].toString().toInt())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEvent(CompanyDetailsEvents.LoadCompany)
        locationUpdateDialog = locationUpdateDialog()
        observeState()
        viewLocationOnMap()
        updatePhoneInfo()
        updateAddressInfo()
        updateEmailInfo()
        updateLocationInfo()
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) {
            updateUi(it)
        }
    }

    private fun updateUi(state: CompanyDetailsState) {
         state.companyUi?.latitude?.let { latitude = it }
         state.companyUi?.longitude?.let { longitude = it }

        binding.companyName.text = state.companyUi?.name
        binding.address.text = if (state.companyUi?.address != "notSet") state.companyUi?.address else "Non attribue"
        binding.phone.text = if (state.companyUi?.phone != "notSet") state.companyUi?.phone else "Non attribue"
        binding.email.text = if (state.companyUi?.email != "notSet") state.companyUi?.email else "Non attribue"
        binding.latitude.text = state.companyUi?.latitude.toString()
        binding.longitude.text = state.companyUi?.longitude.toString()
        if (state.companyUi?.logoPath != "notSet")
            Glide.with(requireContext()).load(state.companyUi?.logoPath).into(binding.companyLogo)

        if (locationUpdateDialog.isShowing) {
            locationUpdateDialog.setMessage("Latitude: ${state.latitude}\nLongitude: ${state.longitude}")
            binding.latitude.text = state.latitude.toString()
            binding.longitude.text = state.longitude.toString()
            locationUpdateDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok") { dialog, _ ->
                viewModel.onEvent(CompanyDetailsEvents.StopLocationUpdates)
                viewModel.onEvent(CompanyDetailsEvents.StopLocationUpdates)
                viewModel.onEvent(
                    CompanyDetailsEvents.UpdateLocation(
                        state.latitude,
                        state.longitude
                    )
                )
                dialog.dismiss()
            }
        }
    }

    private fun viewLocationOnMap() {
        binding.viewLocationBtn.setOnClickListener {
            val uri = Uri.parse("http://maps.google.com/maps?q=loc:$latitude,$longitude(Entreprise)")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(requireContext().packageManager) != null)
                startActivity(intent)
            else
                Toast.makeText(requireContext(), "Aucune application disponible pour visualiser les coordonnes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePhoneInfo() {
        binding.editPhoneBtn.setOnClickListener {
            createDialog("Numero de telephone", InputType.TYPE_CLASS_NUMBER, binding.phone).show()
        }
    }

    private fun updateAddressInfo() {
        binding.editAddressBtn.setOnClickListener {
            createDialog("Adresse", InputType.TYPE_CLASS_TEXT, binding.address).show()
        }
    }

    private fun updateEmailInfo() {
        binding.editEmailBtn.setOnClickListener {
            createDialog("Adresse mail", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, binding.email).show()
        }
    }

    private fun updateLocationInfo() {
        binding.editLocalisationBtn.setOnClickListener {
            locationUpdateDialog.show()
            viewModel.onEvent(CompanyDetailsEvents.LocationUpdate)
        }
    }

    private fun locationUpdateDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Mise a jour localisation")
            .setMessage("Chargement...")
            .create()
    }

    private fun createDialog(hint: String, inputType: Int, textView: TextView): AlertDialog {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val inflater = LayoutInflater.from(requireContext())
        val customBinding = CustomEditTextViewBinding.inflate(inflater)
        customBinding.textHeader.text = hint
        customBinding.prospectorNameTextInput.hint = hint
        customBinding.prospectorNameTextInput.editText?.inputType = inputType
        customBinding.acceptBtn.setOnClickListener {
            val text = customBinding.prospectorNameTextInput.editText!!.text.toString()
            if (text.isEmpty())
                Toast.makeText(requireContext(), "Remplissez le champs", Toast.LENGTH_SHORT).show()
            else {
                //viewModel.onEvent(AddProspectorEvents.UpdateProspector(text, prospectorId))
                textView.text = text
                if (hint == "Adresse mail")
                    viewModel.onEvent(CompanyDetailsEvents.UpdateEmail(text))
                else if (hint == "Adresse")
                    viewModel.onEvent(CompanyDetailsEvents.UpdateAddress(text))
                else if (hint == "Numero de telephone")
                    viewModel.onEvent(CompanyDetailsEvents.UpdatePhone(text))
                dialog.dismiss()
            }
        }
        customBinding.cancelBtn.setOnClickListener { dialog.dismiss() }
        dialog.setView(customBinding.root)
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}