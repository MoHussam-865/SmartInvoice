package com.android_a865.estimatescalculator.core.domain.use_cases.invoice

data class InvoiceUseCases(
    val addInvoice: AddInvoiceUseCase,
    val getInvoices: GetInvoicesUseCase,
    val updateInvoice: UpdateInvoiceUseCase,
    val applyDiscountUseCase: ApplyDiscountUseCase
)
