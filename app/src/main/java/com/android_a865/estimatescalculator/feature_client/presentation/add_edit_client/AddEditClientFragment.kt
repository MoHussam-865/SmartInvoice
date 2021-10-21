package com.android_a865.estimatescalculator.feature_client.presentation.add_edit_client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentAddEditClientBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditClientFragment : Fragment(R.layout.fragment_add_edit_client) {

    private val viewModel by viewModels<AddEditClientViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentAddEditClientBinding.bind(view)

        binding.apply {
            etClientName.editText?.setText(viewModel.clientName)
            etOrganization.editText?.setText(viewModel.org)
            etTitle.editText?.setText(viewModel.title)
            etPhone1.editText?.setText(viewModel.phone1)
            etPhone2.editText?.setText(viewModel.phone2)
            etEmail.editText?.setText(viewModel.email)
            etAddress.editText?.setText(viewModel.address)

            etClientName.editText?.addTextChangedListener {
                viewModel.clientName = it.toString()
            }
            etOrganization.editText?.addTextChangedListener {
                viewModel.org = it.toString()
            }
            etTitle.editText?.addTextChangedListener {
                viewModel.title = it.toString()
            }
            etPhone1.editText?.addTextChangedListener {
                viewModel.phone1 = it.toString()
            }
            etPhone2.editText?.addTextChangedListener {
                viewModel.phone2 = it.toString()
            }
            etEmail.editText?.addTextChangedListener {
                viewModel.email = it.toString()
            }
            etAddress.editText?.addTextChangedListener {
                viewModel.address = it.toString()
            }

            fab.setOnClickListener {
                viewModel.onSaveClicked()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.windowEvents.collect { event ->
                when (event) {
                    is AddEditClientViewModel.WindowEvents.NavigateBack -> {
                        findNavController().popBackStack()
                        true
                    }
                    is AddEditClientViewModel.WindowEvents.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                        true
                    }
                    is AddEditClientViewModel.WindowEvents.Navigate -> {
                        findNavController().navigate(event.direction)
                        true
                    }
                }.exhaustive
            }
        }

    }
}