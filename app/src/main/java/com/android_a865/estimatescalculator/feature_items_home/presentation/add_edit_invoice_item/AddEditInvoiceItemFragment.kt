package com.android_a865.estimatescalculator.feature_items_home.presentation.add_edit_invoice_item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentAddEditInvoiceItemBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditInvoiceItemFragment : Fragment(R.layout.fragment_add_edit_invoice_item) {

    private val viewModel by viewModels<AddEditInvoiceItemViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentAddEditInvoiceItemBinding.bind(view)

        binding.apply {
            itemNameEt.editText?.setText(viewModel.itemName)
            itemPriceEt.editText?.setText(viewModel.itemPrice)
            itemQtyEt.editText?.setText(viewModel.itemQty)

            itemNameEt.editText?.isEnabled = viewModel.isNew

            itemNameEt.editText?.addTextChangedListener {
                viewModel.itemName = it.toString()
            }
            itemPriceEt.editText?.addTextChangedListener {
                viewModel.itemPrice = it.toString()
            }
            itemQtyEt.editText?.addTextChangedListener {
                viewModel.itemQty = it.toString()
            }

            fab.setOnClickListener {
                viewModel.onSaveClicked()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.windowEvents.collect { event ->
                when (event) {
                    is AddEditInvoiceItemViewModel.AddEditItemEvent.NavigateBackWithResult -> {
                        setFragmentResult(
                            "invoice_item",
                            bundleOf("item" to event.item)
                        )
                        findNavController().popBackStack()
                        true
                    }
                    is AddEditInvoiceItemViewModel.AddEditItemEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                        true
                    }
                }.exhaustive
            }
        }

    }

}