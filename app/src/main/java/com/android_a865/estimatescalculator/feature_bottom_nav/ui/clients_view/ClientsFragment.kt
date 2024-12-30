package com.android_a865.estimatescalculator.feature_bottom_nav.ui.clients_view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.core.ui.adapters.ClientsAdapter
import com.android_a865.estimatescalculator.core.utils.exhaustive
import com.android_a865.estimatescalculator.core.utils.hideBottomNav
import com.android_a865.estimatescalculator.core.utils.onTextChanged
import com.android_a865.estimatescalculator.core.utils.setUpActionBarWithNavController
import com.android_a865.estimatescalculator.databinding.FragmentClientsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClientsFragment : Fragment(R.layout.fragment_clients),
    ClientsAdapter.OnItemEventListener {

    private val viewModel by viewModels<ClientsViewModel>()
    private val clientsAdapter = ClientsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        hideBottomNav(false)

        val binding = FragmentClientsBinding.bind(view)

        binding.apply {

            clientsList.apply {
                adapter = clientsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fab.setOnClickListener {
                viewModel.onFabClicked()
            }

            viewModel.clients.asLiveData().observe(viewLifecycleOwner) {
                clientsAdapter.submitList(it)
                tvEmpty.isVisible = it.isEmpty()
            }
        }



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.windowEvents.collect { event ->
                when (event) {
                    is ClientsViewModel.WindowEvents.Navigate -> {
                        hideBottomNav()
                        findNavController().navigate(event.direction)
                    }
                }.exhaustive

            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.clients_view_options, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.onTextChanged {
            viewModel.searchQuery.value = it
        }
    }


    override fun onItemClicked(client: Client) {
        viewModel.onItemClicked(client)
    }
}