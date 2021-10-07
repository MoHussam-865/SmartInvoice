package com.android_a865.estimatescalculator.presentation.pdf_preview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android_a865.estimatescalculator.R
import com.android_a865.estimatescalculator.databinding.FragmentViewPdfBinding
import com.github.barteksc.pdfviewer.PDFView
import java.io.FileNotFoundException
import java.io.InputStream


class ViewPdfFragment : Fragment(R.layout.fragment_view_pdf) {

    lateinit var pdfView: PDFView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentViewPdfBinding.bind(view)
        pdfView = binding.pdfView

        savedInstanceState?.getString("fileName")?.let {
            openPdf(it)
        }
    }

    private fun openPdf(fileName: String) {
        try {
            val pdfFile: InputStream = requireContext().openFileInput(fileName)
            pdfView.recycle()
            pdfView.fromStream(pdfFile)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true) // double tap to zoom
                .onDraw { canvas, pageWidth, pageHeight, displayedPage -> }
                .onDrawAll { canvas, pageWidth, pageHeight, displayedPage -> }
                .onPageError { page, t -> }
                .onPageChange { page, pageCount -> }
                .onTap { true }
                .onRender { nbPages, pageWidth, pageHeight -> pdfView.fitToWidth() }
                .enableAnnotationRendering(true)
                .invalidPageColor(Color.GRAY)
                .load()

        } catch (e: FileNotFoundException) {
            Log.d("Error Opening Pdf" , "$fileName ${e.message}")
        }
    } // open in internal pdfViewer

}