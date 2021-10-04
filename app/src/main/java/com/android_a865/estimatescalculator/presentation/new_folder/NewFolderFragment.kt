package com.android_a865.estimatescalculator.presentation.new_folder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentNewFolderBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewFolderFragment : Fragment(R.layout.fragment_new_folder) {

    private val viewModel by viewModels<NewFolderViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentNewFolderBinding.bind(view)

        binding.apply {
            folderNameEt.setText(viewModel.folderName)
            folderNameEt.addTextChangedListener {
                viewModel.folderName = it.toString()
            }

            fab.setOnClickListener {
                viewModel.onSaveClicked()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditItemEvent.collect { event ->
                when (event) {
                    is NewFolderViewModel.AddEditItemEvent.NavigateBackWithResult -> {
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