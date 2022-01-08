package com.android_a865.estimatescalculator.utils

import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.android_a865.estimatescalculator.R

fun Fragment.setUpActionBarWithNavController() {
    view?.findViewById<Toolbar>(R.id.mainToolBar)?.let {
        appCompatActivity.setSupportActionBar(it)
        it.setupWithNavController(findNavController())
    }
}

fun RecyclerView.scrollToEnd() = smoothScrollToPosition(adapter?.itemCount ?: 0)

val Fragment.appCompatActivity get() = (activity as AppCompatActivity)

fun Fragment.showMessage(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun EditText.setTextWithCursor(str: String) {

    val newTextLength = str.length
    val oldTextLength = text.toString().length
    val diff = if (newTextLength > oldTextLength) 1 else -1
    val newSelection = selectionStart + diff

    setText(str)

    if (newSelection >= 0) {
        setSelection(newSelection)
    }
    //Log.d("view util", "new = $str, old = $text, $diff, selection = $newSelection")
}

// Search View
inline fun SearchView.onTextChanged(crossinline listener: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}