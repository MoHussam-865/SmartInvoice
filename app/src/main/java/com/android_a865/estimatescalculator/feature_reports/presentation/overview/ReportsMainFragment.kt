package com.android_a865.estimatescalculator.feature_reports.presentation.overview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentReportsMainBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ReportsMainFragment : Fragment(R.layout.fragment_reports_main) {

    private val viewModel by viewModels<ReportsViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()

        val binding = FragmentReportsMainBinding.bind(view)

        binding.apply {

            viewModel.numbers.observe(viewLifecycleOwner) {
                tvInvoicesNumber.text = it.invoices.toString()
                tvEstimatesNumber.text = it.estimates.toString()
                tvDraftNumber.text = it.drafts.toString()
            }

            viewModel.totalMoney.observe(viewLifecycleOwner) {
                tvTotalMoney.text = "$it"
            }

            viewModel.clientsCount.observe(viewLifecycleOwner) {
                tvClientsNumber.text = "$it"
            }

            viewModel.itemsCount.observe(viewLifecycleOwner) {
                tvItemsNumber.text = "$it"
            }

            btnViewClients.setOnClickListener {
                viewModel.onViewClientsClicked()
            }

            btnViewItems.setOnClickListener {
                viewModel.onViewItemsClicked()
            }


        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.windowEvents.collect { event ->
                when (event) {
                    is ReportsViewModel.WindowEvents.NavigateTo -> {
                        findNavController().navigate(event.direction)
                    }
                }.exhaustive
            }
        }
    }
}