package com.android_a865.estimatescalculator.feature_main.presentation.items_choose

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.common.adapters.ChooseInvoiceItemsAdapter
import com.android_a865.estimatescalculator.common.adapters.ChosenItemsAdapter
import com.android_a865.estimatescalculator.common.adapters.PathIndicatorAdapter
import com.android_a865.estimatescalculator.databinding.FragmentItemsChooseBinding
import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.scrollToEnd
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ItemsChooseFragment : Fragment(R.layout.fragment_items_choose),
    ChooseInvoiceItemsAdapter.OnItemEventListener,
    ChosenItemsAdapter.OnItemEventListener {

    private val viewModel by viewModels<ItemsChooseViewModel>()
    private val itemsAdapter = ChooseInvoiceItemsAdapter(this)
    private val chosenItemsAdapter = ChosenItemsAdapter(this)
    private val pathIndicator = PathIndicatorAdapter()


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentItemsChooseBinding.bind(view)

        binding.apply {

            itemsList.apply {
                adapter = itemsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            pathList.apply {
                adapter = pathIndicator
                setHasFixedSize(true)
            }

            chosenItemsList.apply {
                adapter = chosenItemsAdapter
                setHasFixedSize(true)
            }

            viewModel.currentPath.asLiveData().observe(viewLifecycleOwner) {
                pathIndicator.submitPath(it)
                pathList.scrollToEnd()
            }

            viewModel.selectedItems.observe(viewLifecycleOwner) {
                chosenItemsList.isVisible = it.isNotEmpty()
                chosenItemsAdapter.submitList(it)
                chosenItemsList.scrollToEnd()

                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "choose_invoice_items", it
                )
            }

            viewModel.itemsData.asLiveData().observe(viewLifecycleOwner) {
                itemsAdapter.submitList(it)
                tvEmpty.isVisible = it.isEmpty()
            }
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackPress()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.itemsWindowEvents.collect { event ->
                when (event) {
                    ItemsChooseViewModel.ItemsWindowEvents.GoBack -> {
                        callback.remove()
                        findNavController().popBackStack()
                        Log.d("ItemsChooseFragment", "this fragment is popped")
                    }
                }.exhaustive
            }
        }

    }


    override fun onItemClicked(item: InvoiceItem) = viewModel.onItemClicked(item)

    override fun onAddItemClicked(item: InvoiceItem) = viewModel.onAddItemClicked(item)

    override fun onMinusItemClicked(item: InvoiceItem) = viewModel.onMinusItemClicked(item)

    override fun onRemoveItemClicked(item: InvoiceItem) = viewModel.onItemRemoveClicked(item)

    override fun onQtySet(item: InvoiceItem, qty: Double) = viewModel.onQtySet(item, qty)
}