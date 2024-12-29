package com.android_a865.estimatescalculator.feature_network.start

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentDefineMeBinding
import com.android_a865.estimatescalculator.feature_network.temp.Role
import com.android_a865.estimatescalculator.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DefineMeFragment : Fragment(R.layout.fragment_define_me) {

    private val viewModel: DefineMeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDefineMeBinding.bind(view)

        binding.apply {

            radio.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_solo -> {
                        viewModel.deviceType.value = Role.Solo
                    }
                    R.id.rb_client -> {
                        viewModel.deviceType.value = Role.Client
                    }
                    R.id.rb_server -> {
                        viewModel.deviceType.value = Role.Server
                    }
                }
            }

            etDeviceName.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.deviceName.value = text.toString()
            }

            fab.setOnClickListener {
                viewModel.onFabClicked()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.windowEvents.collect { event ->
                    when (event) {
                        DefineMeViewModel.WindowEvents.GoBack -> {
                            findNavController().popBackStack()
                            true
                        }
                        DefineMeViewModel.WindowEvents.SetValues -> {
                            etDeviceName.editText?.setText(viewModel.deviceName.value)
                            when (viewModel.deviceType.value) {
                                Role.Solo ->  radio.check(R.id.rb_solo)
                                Role.Client ->  radio.check(R.id.rb_client)
                                Role.Server ->  radio.check(R.id.rb_server)
                                null -> { }
                            }.exhaustive
                            true
                        }
                    }.exhaustive
                }
            }

        }
    }
}