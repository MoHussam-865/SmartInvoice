package com.android_a865.estimatescalculator.core.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


interface PathUtil {
    val name: String
    val path: Path
    val isFolder: Boolean

    fun open(): Path = if (isFolder) path.open(name) else path
}


@Parcelize
data class Path(
    val path: String = ROOT
) : Parcelable {
    /** a new abstraction layer to deal with paths (navigation) */

    fun open(name: String) = Path(path + SEPARATION + name)

    fun pathOf(name: String) = path + SEPARATION + name

    fun back() = Path(parentPath)

    val isRoot get() = (path == ROOT)

    val folders get() = path.replaceFirst(ROOT, "Items").split(SEPARATION)

    fun fullName(name: String): String {
        val list = folders.drop(1).toMutableList()
        list.add(name)
        return list.reversed().joinToString(" ")
    }

    //val parentName get() = _folders.run { get(lastIndex) }

    private val parentPath get() = _folders.dropLast(1).joinToString(separator = SEPARATION)

    private val _folders get() = path.split(SEPARATION)

    companion object {
        private const val SEPARATION = "/"
        private const val ROOT = "."
        //const val NO_PATH = ""
        //const val REDUNDANT = "*"
    }
}

