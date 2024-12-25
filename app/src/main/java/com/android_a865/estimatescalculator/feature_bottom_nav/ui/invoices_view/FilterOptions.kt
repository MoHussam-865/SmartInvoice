package com.android_a865.estimatescalculator.feature_bottom_nav.ui.invoices_view

sealed class FilterOptions {
    object All : FilterOptions()
    object Invoice: FilterOptions()
    object Estimate: FilterOptions()
    object Draft: FilterOptions()
}
