package com.android_a865.estimatescalculator.presentation.selecting

import android.os.Bundle
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
import com.android_a865.estimatescalculator.common.adapters.ItemsAdapter
import com.android_a865.estimatescalculator.common.adapters.PathIndicatorAdapter
import com.android_a865.estimatescalculator.domain.Item
import com.android_a865.estimatescalculator.databinding.FragmentSelectingBinding
import com.android_a865.estimatescalculator.utils.appCompatActivity
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SelectingFragment : Fragment(R.layout.fragment_selecting),
ItemsAdapter.OnItemEventListener {

    private val itemsViewModule by viewModels<ItemsSelectViewModel>()
    private val itemsAdapter = ItemsAdapter(this,true)
    private val pathIndicator = PathIndicatorAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentSelectingBinding.bind(view)

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
        }

        pathIndicator.submitPath(itemsViewModule.currentPath)


        itemsViewModule.itemsData.observe(viewLifecycleOwner) {
            itemsAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            itemsViewModule.itemsWindowEvents.collect { event ->
                when (event) {
                    is ItemsSelectViewModel.ItemsWindowEvents.Navigate -> {
                        findNavController().navigate(event.directions)
                    }
                    is ItemsSelectViewModel.ItemsWindowEvents.NotifyAdapter -> {
                        itemsAdapter.notifyItemChanged(event.position)
                    }
                    ItemsSelectViewModel.ItemsWindowEvents.NavigateBack -> {
                        val x = findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.select_items_options, menu)

        itemsViewModule.numSelected.asLiveData().observe(viewLifecycleOwner){
            appCompatActivity.supportActionBar?.title = it.toString()
            menu.findItem(R.id.edit).isVisible = it == 1
            menu.findItem(R.id.delete).isVisible = it != 0
            menu.findItem(R.id.selectAll).isVisible = it < itemsViewModule.all
            menu.findItem(R.id.deselectAll).isVisible = it == itemsViewModule.all
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.edit -> {
                itemsViewModule.onEditOptionSelected()
                true
            }

            R.id.delete -> {
                itemsViewModule.onDeleteOptionSelected(requireContext())
                true
            }

            R.id.selectAll -> {
                itemsViewModule.onSelectAllChanged(true)
                true
            }

            R.id.deselectAll -> {
                itemsViewModule.onSelectAllChanged(false)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onItemClicked(item: Item, position: Int) =
        itemsViewModule.onItemClicked(item, position)
    override fun onSelectionChange(item: Item, position: Int, b:Boolean) =
        itemsViewModule.onSelectChanged(item, position, b)
    override fun onItemLongClick(item: Item) { }

}