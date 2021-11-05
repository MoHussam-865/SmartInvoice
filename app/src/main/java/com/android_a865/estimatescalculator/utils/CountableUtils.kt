package com.android_a865.estimatescalculator.utils

import com.android_a865.estimatescalculator.feature_main.domain.model.InvoiceItem


fun List<InvoiceItem>.addOneOf(item: InvoiceItem): List<InvoiceItem> {
    var found = false
    val items = this.map {
        if (item.id == it.id) {
            found = true
            it.run { copy(qty = qty + 1) }
        } else it
    }.toMutableList()

    if (!found) items.add(item.run { copy(qty = qty + 1) })
    return items.toList()
}

fun List<InvoiceItem>.addOf(item: InvoiceItem): List<InvoiceItem> {
    var found = false
    val items = this.map {
        if (item.id == it.id) {
            found = true
            it.run { copy(qty = qty + item.qty) }
        } else it
    }.toMutableList()

    if (!found) items.add(item)
    return items.toList()
}

fun List<InvoiceItem>.removeOneOf(item: InvoiceItem): List<InvoiceItem> = map {

    if (item.id == it.id) {
        it.run { copy(qty = qty - 1) }
    } else it

}.filtered


fun List<InvoiceItem>.removeAllOf(item: InvoiceItem): List<InvoiceItem> = filter {
    it.id != item.id
}.toList()

fun List<InvoiceItem>.setQtyTo(item: InvoiceItem, myQty: Double): List<InvoiceItem> {
    var found = false

    val items = this.map {
        if (item.id == it.id) {
            found = true
            it.run { copy(qty = myQty) }
        } else it
    }.toMutableList()

    if (!found) items.add(item.run { copy(qty = myQty) })
    return items.toList().filtered
}

fun List<InvoiceItem>.include(selectedItems: List<InvoiceItem>): List<InvoiceItem> = map { item ->
    selectedItems.filter {
        it.id == item.id
    }.run {
        if (size == 1) first() else item
    }
}


val List<InvoiceItem>.filtered get() = filter { it.qty > 0.0 }


fun <T> List<T>.remove(position: Int): List<T> = this.run {
    toMutableList().removeAt(position)
    toList()
}


fun <T> List<T>?.update(position: Int, fun0: (T) -> T): List<T> = orEmpty().run {
    toMutableList()[position] = fun0(this[position])
    toList()
}



