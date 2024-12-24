package com.android_a865.estimatescalculator.feature_network.start

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentDefineMeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DefineMeFragment : Fragment() {

    private val viewModel: DefineMeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDefineMeBinding.bind(view)

        binding.apply {

        }

    }
}