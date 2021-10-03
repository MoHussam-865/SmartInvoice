package com.android_a865.estimatescalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_a865.estimatescalculator.database.domain.InvoiceItem
import com.android_a865.estimatescalculator.databinding.AdapterNewEstimateBinding

class InvoiceItemsAdapter(
    private val listener: OnItemEventListener,
) : ListAdapter<InvoiceItem, InvoiceItemsAdapter.ViewHolder>(ItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        AdapterNewEstimateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(private val binding: AdapterNewEstimateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                itemRemove.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onItemRemoveClicked(item)
                    }
                }

                plus.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onPlusClicked(item)
                    }
                }

                minus.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onMinusClicked(item)
                    }
                }

                etQty.addTextChangedListener { text ->
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onQtyChanged(item, text.toString())
                    }
                }


            }
        }

        fun bind(item: InvoiceItem) {
            binding.apply {

                itemName.text = item.name
                itemUnitPrice.text = item.price.toString()
                itemTotal.text = item.total.toString()
                etQty.setText(item.qty.toString())

            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<InvoiceItem>() {
        override fun areItemsTheSame(oldItem: InvoiceItem, newItem: InvoiceItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InvoiceItem, newItem: InvoiceItem): Boolean =
            oldItem == newItem
    }


    interface OnItemEventListener {
        fun onItemRemoveClicked(item: InvoiceItem)
        fun onPlusClicked(item: InvoiceItem)
        fun onMinusClicked(item: InvoiceItem)
        fun onQtyChanged(item: InvoiceItem, text: String)
    }

}