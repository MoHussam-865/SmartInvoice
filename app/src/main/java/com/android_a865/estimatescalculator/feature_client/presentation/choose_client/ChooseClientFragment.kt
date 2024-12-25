package com.android_a865.estimatescalculator.feature_client.presentation.choose_client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.common.adapters.ClientsAdapter
import com.android_a865.estimatescalculator.databinding.FragmentChooseClientBinding
import com.android_a865.estimatescalculator.feature_client.domain.model.Client
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseClientFragment : Fragment(R.layout.fragment_choose_client),
    ClientsAdapter.OnItemEventListener {

    private val viewModel by viewModels<ChooseClientViewModel>()
    private val clientsAdapter = ClientsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()
        val binding = FragmentChooseClientBinding.bind(view)

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
                    is ChooseClientViewModel.WindowEvents.Navigate -> {
                        findNavController().navigate(event.direction)
                        true
                    }
                    is ChooseClientViewModel.WindowEvents.NavigateBackWithResult -> {
                        setFragmentResult(
                            "chosen_client",
                            bundleOf("client" to event.client)
                        )
                        findNavController().popBackStack()
                        true
                    }
                }.exhaustive

            }
        }


    }

    override fun onItemClicked(client: Client) {
        viewModel.onItemClicked(client)
    }
}