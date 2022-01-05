package com.android_a865.estimatescalculator.feature_reports.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentReportsMainBinding
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsMainFragment : Fragment(R.layout.fragment_reports_main) {

    private val viewModel by viewModels<ReportsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentReportsMainBinding.bind(view)

        binding.apply {
            viewModel.numbers.observe(viewLifecycleOwner) {
                invoicesNumber.text = it.invoices.toString()
                estimatesNumber.text = it.estimates.toString()
                draftNumber.text = it.drafts.toString()
            }

            viewModel.totalMoney.observe(viewLifecycleOwner) {
                totalMoney.text = it.toString()
            }
        }

    }
}