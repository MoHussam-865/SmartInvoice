package com.android_a865.estimatescalculator.presentation.new_estimate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.FileUtils.copy
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.common.adapters.InvoiceItemsAdapter
import com.android_a865.estimatescalculator.databinding.FragmentNewEstimateBinding
import com.android_a865.estimatescalculator.domain.model.InvoiceItem
import com.android_a865.estimatescalculator.presentation.main_page.MainFragmentViewModel
import com.android_a865.estimatescalculator.utils.AUTHORITY
import com.android_a865.estimatescalculator.utils.exhaustive
import com.android_a865.estimatescalculator.utils.setUpActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
class NewEstimateFragment : Fragment(R.layout.fragment_new_estimate),
    InvoiceItemsAdapter.OnItemEventListener {

    private val viewModel by viewModels<NewEstimateViewModel>()
    private val itemsAdapter = InvoiceItemsAdapter(this)

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBarWithNavController()

        val binding = FragmentNewEstimateBinding.bind(view)
        binding.apply {

            listItem.apply {
                adapter = itemsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(false)
            }

            addItems.setOnClickListener {
                findNavController().navigate(
                    NewEstimateFragmentDirections.actionNewEstimateFragmentToItemsChooseFragment(
                        viewModel.itemsFlow.value.toTypedArray()
                    )
                )
            }

            fab.setOnClickListener {
                viewModel.onSaveClicked()
            }

            viewModel.itemsFlow.asLiveData().observe(viewLifecycleOwner) {
                itemsAdapter.submitList(it)
            }

            viewModel.totalFlow.asLiveData().observe(viewLifecycleOwner) {
                itemsTotal.text = it.toString()
            }

        }

        viewModel.onItemsSelected(
            findNavController()
                .currentBackStackEntry
                ?.savedStateHandle
                ?.get<List<InvoiceItem>>(
                    "choose_invoice_items"
                )
        )

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.invoiceWindowEvents.collect { event ->
                when (event) {
                    is NewEstimateViewModel.InvoiceWindowEvents.NavigateBack -> {
                        findNavController().popBackStack()
                        true
                    }
                    is NewEstimateViewModel.InvoiceWindowEvents.OpenPdfExternally -> {
                        openPdfExternal(event.fileName)
                        true
                    }
                    is NewEstimateViewModel.InvoiceWindowEvents.SendPdf -> {
                        sendPdf(event.fileName)
                        true
                    }
                    is NewEstimateViewModel.InvoiceWindowEvents.ShowMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                        true
                    }
                }.exhaustive

            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.estimate_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_pdf -> {
                viewModel.onOpenPdfClicked(context)
                true
            }

            R.id.send_pdf -> {
                viewModel.onSendPdfClicked(context)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onItemRemoveClicked(item: InvoiceItem) = viewModel.onItemRemoveClicked(item)

    override fun onPlusClicked(item: InvoiceItem) = viewModel.onOneItemAdded(item)

    override fun onMinusClicked(item: InvoiceItem) = viewModel.onOneItemRemoved(item)

    override fun onQtyChanged(item: InvoiceItem, text: String) =
        viewModel.onItemQtyChanged(item, text)


    fun openPdfExternal(fileName: String) {
        try {
            context?.let {
                val file = getFile(it, fileName)

                val uri = FileProvider.getUriForFile(it, AUTHORITY, file)

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(intent, "Send with"))
            }

        } catch (e: Exception) {
            Log.d("pdf_error", e.message.toString())
        }

    } // open in external pdfViewer

    @Throws(IOException::class)
    private fun getFile(context: Context, fileName: String): File {

        Log.d("pdf_error", fileName)
        val tempFile = File.createTempFile(".tempInvoice", fileName)
        tempFile.deleteOnExit()
        val os = FileOutputStream(tempFile)
        val inputStream: InputStream = context.openFileInput(fileName)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            copy(inputStream, os)
        }
        return tempFile
    }

    fun sendPdf(fileName: String) {
        try {
            context?.let {
                val file = getFile(it, fileName)

                val uri = FileProvider.getUriForFile(it, AUTHORITY, file)

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "application/pdf"
                intent.putExtra(Intent.EXTRA_SUBJECT, file.name)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intent, "Send With"))
            }

        } catch (e: Exception) {
            Log.d("NewEstimateFragment pdf", e.message.toString())
        }
    }

}