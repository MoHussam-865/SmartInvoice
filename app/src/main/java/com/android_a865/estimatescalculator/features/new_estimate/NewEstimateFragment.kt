package com.android_a865.estimatescalculator.features.new_estimate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.adapters.InvoiceItemsAdapter
import com.android_a865.estimatescalculator.database.domain.InvoiceItem
import com.android_a865.estimatescalculator.databinding.FragmentNewEstimateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewEstimateFragment : Fragment(R.layout.fragment_new_estimate),
InvoiceItemsAdapter.OnItemEventListener {

    private val viewModel by viewModels<NewEstimateViewModel>()
    private val itemsAdapter = InvoiceItemsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                                viewModel.items.value.orEmpty().toTypedArray()
                        )
                )
            }

            viewModel.items.observe(viewLifecycleOwner) {
                itemsAdapter.submitList(it)
            }

            viewModel.total.observe(viewLifecycleOwner) {
                itemsTotal.text = it.toString()
            }

        }

        val observed = "choose_invoice_items"
        findNavController().apply {
            currentBackStackEntry?.savedStateHandle?.getLiveData<List<InvoiceItem>>(
                    observed
            )?.observe(viewLifecycleOwner) {
                viewModel.onItemsSelected(it)
                currentBackStackEntry?.savedStateHandle?.set(observed, null)
            }
        }

    }



    override fun onItemRemoveClicked(item: InvoiceItem) = viewModel.onItemRemoveClicked(item)

    override fun onPlusClicked(item: InvoiceItem) = viewModel.onOneItemAdded(item)

    override fun onMinusClicked(item: InvoiceItem) = viewModel.onOneItemRemoved(item)

    override fun onQtyChanged(item: InvoiceItem, text: String) = viewModel.onItemQtyChanged(item, text)

}