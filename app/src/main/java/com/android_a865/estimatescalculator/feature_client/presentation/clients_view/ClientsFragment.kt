package com.android_a865.estimatescalculator.feature_client.presentation.clients_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.common.adapters.ClientsAdapter
import com.android_a865.estimatescalculator.databinding.FragmentClientsBinding
import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.feature_main.presentation.main_page.MainFragmentViewModel
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ClientsFragment : Fragment(R.layout.fragment_clients),
    ClientsAdapter.OnItemEventListener {

    private val viewModel by viewModels<ClientsViewModel>()
    private val clientsAdapter = ClientsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
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
                        findNavController().navigate(event.direction)
                    }
                }.exhaustive

            }
        }

        setHasOptionsMenu(false)
    }

    override fun onItemClicked(client: Client) {
        viewModel.onItemClicked(client)
    }
}