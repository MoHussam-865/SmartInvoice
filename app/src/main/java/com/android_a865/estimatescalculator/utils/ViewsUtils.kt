package com.android_a865.estimatescalculator.utils

import androidx.appcompat.app.AppCompatActivity
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
