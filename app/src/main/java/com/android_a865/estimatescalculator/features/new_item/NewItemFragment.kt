package com.android_a865.estimatescalculator.features.new_item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentNewItemBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewItemFragment : Fragment(R.layout.fragment_new_item) {

    private val viewModel by viewModels<NewItemViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentNewItemBinding.bind(view)

        binding.apply {
            itemNameEt.setText(viewModel.itemName)
            itemPriceEt.setText(viewModel.itemPrice)
            itemNameEt.addTextChangedListener {
                viewModel.itemName = it.toString()
            }

            itemPriceEt.addTextChangedListener {
                viewModel.itemPrice = it.toString()
            }

            fab.setOnClickListener {
                viewModel.onSaveClicked()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditItemEvent.collect { event ->
                when (event) {
                    is NewItemViewModel.AddEditItemEvent.NavigateBackWithResult -> {
                        findNavController().popBackStack()
                        true
                    }
                    is NewItemViewModel.AddEditItemEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        true
                    }
                    NewItemViewModel.AddEditItemEvent.NavigateBackWithResult2 -> {
                        findNavController().navigate(
                            NewItemFragmentDirections.actionNewItemFragmentToMainFragment2()
                        )
                        true
                    }
                }.exhaustive
            }
        }
    }
}