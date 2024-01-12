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
import com.android_a865.estimatescalculator.feature_settings.domain.models.AppSettings
import com.android_a865.estimatescalculator.utils.DATE_FORMATS
import com.android_a865.estimatescalculator.utils.date
import com.android_a865.estimatescalculator.utils.toFormattedString

class InvoicesAdapter(
    private val listener: OnItemEventListener,
) : ListAdapter<Invoice, InvoicesAdapter.ViewHolder>(InvoiceDiffCallback()) {

    private lateinit var context: Context
    private var appSettings: AppSettings? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            AdapterInvoiceViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setAppSettings(appSettings: AppSettings) {
        this.appSettings = appSettings
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(private val binding: AdapterInvoiceViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    listener.onItemClicked(item)
                }
            }

        }

        fun bind(invoice: Invoice) {
            binding.apply {
                tvInvoiceType.text = invoice.type.name
                tvInvoiceDate.text = invoice.date.date(appSettings?.dateFormat ?: DATE_FORMATS[0])
                tvClientName.text = invoice.client?.name
                invoiceTotal.text = invoice.total.toFormattedString()

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