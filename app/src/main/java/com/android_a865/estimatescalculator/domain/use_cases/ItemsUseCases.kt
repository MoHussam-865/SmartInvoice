package com.android_a865.estimatescalculator.domain.use_cases

data class ItemsUseCases(
    val getItems: GetItemsUseCase,
    val getInvoiceItems: GetInvoiceItemsUseCase,
    val getItemByID: GetItemByIDUseCase,
    val deleteItems: DeleteItemsUseCase,
    val addItem: AddItemUseCase,
    val updateItem: UpdateItemUseCase,

)
