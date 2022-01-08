package com.android_a865.estimatescalculator.feature_reports.presentation.report_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentReportListBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentReportList : Fragment(R.layout.fragment_report_list) {

    private val viewModel by viewModels<ReportListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()

        val binding = FragmentReportListBinding.bind(view)

        binding.apply {

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.windowEvents.collect { event ->
                when (event) {
                    is ReportListViewModel.WindowEvents.ShowMessage -> {
                        Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }
    }
}