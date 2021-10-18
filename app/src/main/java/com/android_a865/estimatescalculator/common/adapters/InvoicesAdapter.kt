package com.android_a865.estimatescalculator.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_a865.estimatescalculator.databinding.AdapterInvoiceViewBinding
import com.android_a865.estimatescalculator.feature_main.domain.model.Invoice
import com.android_a865.estimatescalculator.utils.date

class InvoicesAdapter(
    private val listener: OnItemEventListener,
) : ListAdapter<Invoice, InvoicesAdapter.ViewHolder>(InvoiceDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            AdapterInvoiceViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(private val binding: AdapterInvoiceViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    listener.onItemClicked(item)
                }
            }

        }

        fun bind(invoice: Invoice) {
            binding.apply {
                invoiceDate.text = invoice.date.date()
                invoiceTotal.text = invoice.total.toString()

                val itemsAdapter = InvoiceItemsViewAdapter().apply {
                    submitList(invoice.items)
                }
                itemsList.apply {
                    adapter = itemsAdapter
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(false)
                }

            }
        }
    }

    class InvoiceDiffCallback : DiffUtil.ItemCallback<Invoice>() {
        override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean =
            oldItem == newItem
    }

    interface OnItemEventListener {
        fun onItemClicked(invoice: Invoice)
    }
}