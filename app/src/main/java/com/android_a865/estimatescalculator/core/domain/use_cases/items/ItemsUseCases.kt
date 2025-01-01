package com.android_a865.estimatescalculator.core.domain.use_cases.items

data class ItemsUseCases(
    val getItems: GetItemsUseCase,
    val getInvoiceItems: GetInvoiceItemsUseCase,
    val getItemByID: GetItemByIDUseCase,
    val deleteItems: DeleteItemsUseCase,
    val addItem: AddItemUseCase,
    val updateItem: UpdateItemUseCase,
    val copyFolderUseCases: CopyFolderUseCase
)
