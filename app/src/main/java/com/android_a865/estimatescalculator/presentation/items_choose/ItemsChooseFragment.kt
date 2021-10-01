package com.android_a865.estimatescalculator.presentation.items_choose

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
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
import com.android_a865.estimatescalculator.domain.InvoiceItem
import com.android_a865.estimatescalculator.databinding.FragmentItemsChooseBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ItemsChooseFragment : Fragment(R.layout.fragment_items_choose),
ChooseInvoiceItemsAdapter.OnItemEventListener,
ChosenItemsAdapter.OnItemEventListener {

    private val itemsViewModel by viewModels<ItemsChooseViewModel>()
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

            itemsViewModel.currentPath.asLiveData().observe(viewLifecycleOwner) {
                pathIndicator.submitPath(it)
                pathList.scrollToEnd()
            }

            itemsViewModel.selectedItems.asLiveData().observe(viewLifecycleOwner) {
                binding.chosenItemsList.isVisible = it.isNotEmpty()
                chosenItemsAdapter.submitList(it)
                chosenItemsList.scrollToEnd()

                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "choose_invoice_items", it
                )
                Log.d("ItemsChooseFragment", "the size = "+it.size)
            }

            itemsViewModel.itemsData.asLiveData().observe(viewLifecycleOwner) {
                itemsAdapter.submitList(it)
            }
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            itemsViewModel.onBackPress()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            itemsViewModel.itemsWindowEvents.collect { event ->
                when (event) {
                    ItemsChooseViewModel.ItemsWindowEvents.GoBack -> {
                        callback.remove()
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }

    }



    override fun onItemClicked(item: InvoiceItem, position: Int) =
            itemsViewModel.onItemClicked(item)

    override fun onAddItemClicked(item: InvoiceItem, position: Int) =
            itemsViewModel.onAddItemClicked(item)

    override fun onMinusItemClicked(item: InvoiceItem, position: Int) =
            itemsViewModel.onMinusItemClicked(item)

    override fun onRemoveItemClicked(item: InvoiceItem) =
            itemsViewModel.onItemRemoveClicked(item)

    override fun onQtySet(item: InvoiceItem, qty: Double) =
            itemsViewModel.onQtySet(item, qty)
}