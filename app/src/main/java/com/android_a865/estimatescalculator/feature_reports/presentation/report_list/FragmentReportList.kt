package com.android_a865.estimatescalculator.feature_reports.presentation.report_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentReportListBinding
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentReportList : Fragment(R.layout.fragment_report_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()

        val binding = FragmentReportListBinding.bind(view)

        binding.apply {

        }
    }
}