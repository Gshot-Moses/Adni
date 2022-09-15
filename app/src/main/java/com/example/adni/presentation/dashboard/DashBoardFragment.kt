package com.example.adni.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adni.R
import com.example.adni.databinding.FragmentDashBoardBinding

class DashBoardFragment: Fragment() {

    private var _binding: FragmentDashBoardBinding? = null
    private val binding: FragmentDashBoardBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.companyBoard.setOnClickListener {
            //findNavController().navigate(R.id.action_dashBoardFragment_to_companyListFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}