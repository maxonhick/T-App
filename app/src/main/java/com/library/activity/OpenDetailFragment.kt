package com.library.activity

import com.library.LibraryObjects

interface OpenDetailFragment {
    fun showDetail(item: LibraryObjects, isNew: Boolean)
}