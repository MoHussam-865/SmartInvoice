package com.android_a865.estimatescalculator.feature_in_app.presentation.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentSubscribeBinding
import com.android_a865.estimatescalculator.utils.MONTHLY_SUBSCRIPTION
import com.android_a865.estimatescalculator.utils.YEARLY_SUBSCRIPTION
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SubscribeFragment : Fragment(R.layout.fragment_subscribe) {

    private val viewModel by activityViewModels<SharedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentSubscribeBinding.bind(view)

        binding.apply {
            btnMonthlySubscription.setOnClickListener {
                viewModel.subscribe(MONTHLY_SUBSCRIPTION)
            }

            btnYearlySubscription.setOnClickListener {
                viewModel.subscribe(YEARLY_SUBSCRIPTION)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.windowEvents.collect { event ->
                when (event) {
                    is SharedViewModel.WindowEvents.LaunchBillingFlow -> {
                        viewModel.launchBillingFlow(requireActivity(), event.flowParams)
                        true
                    }
                    is SharedViewModel.WindowEvents.ShowMessage -> {
                        Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                        true
                    }
                }.exhaustive
            }
        }

    }
}