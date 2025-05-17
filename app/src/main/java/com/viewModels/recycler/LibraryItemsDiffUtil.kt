package com.viewModels.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.library.LibraryObjects

class LibraryItemsDiffUtil: DiffUtil.ItemCallback<LibraryObjects>() {
    override fun areItemsTheSame(oldItem: LibraryObjects, newItem: LibraryObjects): Boolean {
        return oldItem.objectId == newItem.objectId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: LibraryObjects, newItem: LibraryObjects): Boolean {
        return oldItem == newItem
    }
}