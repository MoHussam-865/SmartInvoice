package com.android_a865.estimatescalculator.utils

interface Selectable<T> {
    var isSelected: Boolean

    fun changeTo(b: Boolean = isSelected):T
    fun changeSelection() { isSelected = !isSelected }
}


val <T> List<Selectable<T>>.numSelected get() = this.filterSelected().size

fun <T> List<Selectable<T>>.selectAll(b: Boolean): List<T> = this.map { it.changeTo(b) }

fun <T> List<Selectable<T>>?.filterSelected(): List<T> = this.orEmpty().filter { it.isSelected }.map {
    it.changeTo()
}

fun <T> List<Selectable<T>>.selectOnlyWhere(fun0: (T) -> Boolean): List<T> = this.map {
    it.changeTo(fun0(it.changeTo()))
}



