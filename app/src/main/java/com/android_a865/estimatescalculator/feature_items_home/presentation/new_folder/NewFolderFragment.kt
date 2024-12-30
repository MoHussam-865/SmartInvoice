package com.android_a865.estimatescalculator.feature_items_home.presentation.new_folder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.core.utils.exhaustive
import com.android_a865.estimatescalculator.core.utils.setUpActionBarWithNavController
import com.android_a865.estimatescalculator.databinding.FragmentNewFolderBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewFolderFragment : Fragment(R.layout.fragment_new_folder) {

    private val viewModel by viewModels<NewFolderViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentNewFolderBinding.bind(view)

        binding.apply {
            folderNameEt.editText?.setText(viewModel.folderName)
            folderNameEt.editText?.addTextChangedListener {
                viewModel.folderName = it.toString()
            }

            if (viewModel.folderDiscount != "0.0") {
                folderDiscountEt.editText?.setText(viewModel.folderDiscount)
            }

            folderDiscountEt.editText?.addTextChangedListener {
                viewModel.folderDiscount = it.toString()
            }

            fab.setOnClickListener {
                viewModel.onSaveClicked()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditItemEvent.collect { event ->
                when (event) {
                    is NewFolderViewModel.AddEditItemEvent.NavigateBackWithResult -> {
                        Toast.makeText(
                            requireContext(),
                            "Folder Saved",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                        true
                    }
                    is NewFolderViewModel.AddEditItemEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        true
                    }
                    NewFolderViewModel.AddEditItemEvent.NavigateBackWithResult2 -> {
                        findNavController().navigate(
                            NewFolderFragmentDirections.actionNewFolderFragmentToMainFragment2(
                                    path = viewModel.path
                            )
                        )
                        true
                    }
                }.exhaustive
            }
        }


    }
}