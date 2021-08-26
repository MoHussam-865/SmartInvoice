package com.android_a865.estimatescalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_a865.estimatescalculator.databinding.PathIndicatorAdapterBinding
import com.android_a865.estimatescalculator.utils.Path

class PathIndicatorAdapter: ListAdapter<String, PathIndicatorAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            PathIndicatorAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))


    fun submitPath(path: Path) = submitList(path.folders)


    class DiffCallback: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    class ViewHolder(private val binding: PathIndicatorAdapterBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String) { binding.folderName.text = name }
    }
}