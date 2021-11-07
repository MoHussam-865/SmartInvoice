package com.android_a865.estimatescalculator.feature_reports.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentReportsMainBinding
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsMainFragment : Fragment(R.layout.fragment_reports_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentReportsMainBinding.bind(view)

        binding.apply {
            btnSubscribe.setOnClickListener {
                findNavController().navigate(
                    ReportsMainFragmentDirections.actionReportsMainFragmentToSubscribeFragment()
                )
            }
        }

    }
}