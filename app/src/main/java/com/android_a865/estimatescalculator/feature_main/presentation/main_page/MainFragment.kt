package com.android_a865.estimatescalculator.feature_main.presentation.main_page

import android.os.Bundle
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
import com.android_a865.estimatescalculator.databinding.FragmentMainBinding
import com.android_a865.estimatescalculator.feature_main.domain.model.Item
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.scrollToEnd
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), ItemsAdapter.OnItemEventListener {

    private val viewModule by viewModels<MainFragmentViewModel>()

    private val itemsAdapter = ItemsAdapter(this)
    private val pathIndicator = PathIndicatorAdapter()

    @ExperimentalCoroutinesApi
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

        viewModule.itemsData.observe(viewLifecycleOwner) {
            itemsAdapter.submitList(it)
        }

        viewModule.currentPath.asLiveData().observe(viewLifecycleOwner){
            pathIndicator.submitPath(it)
            binding.pathList.scrollToEnd()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModule.onBackPressed()
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModule.windowEvents.collect { event ->
                when (event) {
                    is MainFragmentViewModel.WindowEvents.CloseTheApp -> {
                        callback.remove()
                        requireActivity().onBackPressed()
                        true
                    }
                    is MainFragmentViewModel.WindowEvents.Navigate -> {
                        findNavController().navigate(event.direction)
                        true
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
                viewModule.onNewItemSelected()
                true
            }

            R.id.newFolder -> {
                viewModule.onNewFolderSelected()
                true
            }

            R.id.newEstimate -> {
                viewModule.onNewEstimateSelected()
                true
            }

            R.id.myEstimate -> {
                viewModule.onMyEstimateSelected()
                true
            }

            R.id.myClients -> {
                viewModule.onMyClientsSelected()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClicked(item: Item, position: Int) =
            viewModule.onItemClicked(item)

    override fun onItemLongClick(item: Item) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragment2ToSelectingFragment(
                path = viewModule.currentPath.value,
                id = item.id
            )
        )
        //itemsViewModule.onItemLongClick(item)
    }

    override fun onSelectionChange(item: Item, position: Int, b: Boolean) { }
}