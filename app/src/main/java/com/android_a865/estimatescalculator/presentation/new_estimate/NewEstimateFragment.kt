package com.android_a865.estimatescalculator.presentation.new_estimate

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.common.adapters.InvoiceItemsAdapter
import com.android_a865.estimatescalculator.databinding.FragmentNewEstimateBinding
import com.android_a865.estimatescalculator.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewEstimateFragment : Fragment(R.layout.fragment_new_estimate),
    InvoiceItemsAdapter.OnItemEventListener {

    private val viewModel by viewModels<NewEstimateViewModel>()
    private val itemsAdapter = InvoiceItemsAdapter(this)

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()

        val binding = FragmentNewEstimateBinding.bind(view)
        binding.apply {

            listItem.apply {
                adapter = itemsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(false)
            }

            addItems.setOnClickListener {
                findNavController().navigate(
                    NewEstimateFragmentDirections.actionNewEstimateFragmentToItemsChooseFragment(
                        viewModel.itemsFlow.value.toTypedArray()
                    )
                )
            }


            viewModel.itemsFlow.asLiveData().observe(viewLifecycleOwner) {
                itemsAdapter.submitList(it)
            }

            viewModel.totalFlow.asLiveData().observe(viewLifecycleOwner) {
                itemsTotal.text = it.toString()
            }

        }

        viewModel.onItemsSelected(
            findNavController()
                .currentBackStackEntry
                ?.savedStateHandle
                ?.get<List<InvoiceItem>>(
                    "choose_invoice_items"
                )
        )

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.invoiceWindowEvents.collect { event ->
                when (event) {
                    is NewEstimateViewModel.InvoiceWindowEvents.NavigateBack -> {
                        findNavController().popBackStack()
                        true
                    }
                    is NewEstimateViewModel.InvoiceWindowEvents.OpenPdf -> {
                        findNavController().navigate(
                            NewEstimateFragmentDirections.actionNewEstimateFragmentToViewPdfFragment(
                                fileName = event.fileName
                            )
                        )
                        Log.d("NewEstimateFragment", "pdf file was created")
                        true
                    }
                    is NewEstimateViewModel.InvoiceWindowEvents.ShowMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                        true
                    }
                }.exhaustive

            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.estimate_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_pdf -> {
                viewModel.onOpenPdfClicked(context)
                true
            }

            R.id.save_invoice -> {
                viewModel.onSaveClicked()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onItemRemoveClicked(item: InvoiceItem) = viewModel.onItemRemoveClicked(item)

    override fun onPlusClicked(item: InvoiceItem) = viewModel.onOneItemAdded(item)

    override fun onMinusClicked(item: InvoiceItem) = viewModel.onOneItemRemoved(item)

    override fun onQtyChanged(item: InvoiceItem, text: String) =
        viewModel.onItemQtyChanged(item, text)


}