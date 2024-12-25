package com.android_a865.estimatescalculator.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_a865.estimatescalculator.databinding.AdapterInvoiceItemsViewBinding
import com.android_a865.estimatescalculator.feature_items_home.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.utils.toFormattedString

class InvoiceItemsViewAdapter : ListAdapter<InvoiceItem, InvoiceItemsViewAdapter.ViewHolder>(ItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        AdapterInvoiceItemsViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(private val binding: AdapterInvoiceItemsViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InvoiceItem) {
            binding.apply {
                itemName.text = item.fullName
                itemUnitPrice.text = item.finalPrice.toFormattedString()
                itemTotal.text = item.total.toFormattedString()
                itemQty.text = item.qty.toFormattedString()
                itemDiscount.text = item.discountDetail
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<InvoiceItem>() {
        override fun areItemsTheSame(oldItem: InvoiceItem, newItem: InvoiceItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: InvoiceItem, newItem: InvoiceItem): Boolean =
            oldItem == newItem
    }

}