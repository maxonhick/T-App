package com.tBankApp.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.library.Book
import com.library.Disk
import com.library.LibraryObjects
import com.library.Newspaper
import com.library.R
import com.library.databinding.LibraryItemBinding
import com.tBankApp.recycler.view_holder.LibraryItemViewHolder

class LibraryAdapter (private val onItemClick: (LibraryObjects) -> Unit): ListAdapter<LibraryObjects, LibraryItemViewHolder>(LibraryItemsDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return when(item) {
            is Book, is Newspaper, is Disk -> R.layout.library_item
            else -> throw IllegalArgumentException("Unknown element: $item")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryItemViewHolder {
        val binding = LibraryItemBinding.inflate(LayoutInflater.from(parent.context))
        return LibraryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LibraryItemViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = currentList.size
}