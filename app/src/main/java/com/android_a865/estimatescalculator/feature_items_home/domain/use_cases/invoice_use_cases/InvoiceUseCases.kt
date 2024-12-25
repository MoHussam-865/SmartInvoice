package com.android_a865.estimatescalculator.feature_items_home.domain.use_cases.invoice_use_cases

data class InvoiceUseCases(
    val addInvoice: AddInvoiceUseCase,
    val getInvoices: GetInvoicesUseCase,
    val updateInvoice: UpdateInvoiceUseCase,
    val applyDiscountUseCase: ApplyDiscountUseCase
)
