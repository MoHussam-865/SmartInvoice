package com.android_a865.estimatescalculator.feature_settings.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentSettingsBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentSettingsBinding.bind(view)

        binding.apply {
            tvDateFormat.setOnClickListener {
                viewModel.onDateFormatSelected(requireContext())
            }

            tvCurrency.setOnClickListener {
                viewModel.onCurrencySelected(requireContext())
            }

            tvCompanyInfo.setOnClickListener {
                viewModel.onCompanyInfoSelected()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.windowEvents.collect { event ->
                when (event) {
                    is SettingsViewModel.WindowEvents.Navigate -> {
                        findNavController().navigate(event.direction)
                    }
                }.exhaustive
            }
        }

    }
}