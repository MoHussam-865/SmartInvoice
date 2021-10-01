package com.android_a865.estimatescalculator.presentation.main_page

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
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
import com.android_a865.estimatescalculator.databinding.FragmentMainBinding
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), ItemsAdapter.OnItemEventListener {

    private val itemsViewModule by viewModels<MainFragmentViewModel>()

    private val itemsAdapter = ItemsAdapter(this)
    private val pathIndicator = PathIndicatorAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()

        val binding = FragmentMainBinding.bind(view)

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

        itemsViewModule.itemsData.observe(viewLifecycleOwner) {
            itemsAdapter.submitList(it)
        }

        itemsViewModule.currentPath.asLiveData().observe(viewLifecycleOwner){
            pathIndicator.submitPath(it)
            binding.pathList.scrollToEnd()
        }


        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            itemsViewModule.onBackPressed()
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            itemsViewModule.itemsWindowEvents.collect { event ->
                when (event) {
                    MainFragmentViewModel.ItemsWindowEvents.CloseTheApp -> {
                        callback.remove()
                        requireActivity().onBackPressed()
                        Log.d("MainFragment", "this function was called")
                    }
                }.exhaustive

            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_page_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.newItem -> {
                findNavController().navigate(
                        MainFragmentDirections.actionMainFragment2ToNewItemFragment(
                                path = itemsViewModule.currentPath.value
                        )
                )
                true
            }

            R.id.newFolder -> {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragment2ToNewFolderFragment(
                        path = itemsViewModule.currentPath.value
                    )
                )
                true
            }

            R.id.newEstimate -> {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragment2ToNewEstimateFragment()
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClicked(item: Item, position: Int) =
            itemsViewModule.onItemClicked(item)

    override fun onItemLongClick(item: Item) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragment2ToSelectingFragment(
                path = itemsViewModule.currentPath.value,
                id = item.id
            )
        )
        //itemsViewModule.onItemLongClick(item)
    }

    override fun onSelectionChange(item: Item, position: Int, b: Boolean) { }
}