package com.tBankApp.recycler.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.library.LibraryObjects
import com.library.R
import com.library.TypeLibraryObjects
import com.library.databinding.LibraryItemBinding

class LibraryItemViewHolder(private val binding: LibraryItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: LibraryObjects) = with(binding) {
        when (item.objectType) {
            TypeLibraryObjects.Book -> image.setImageResource(R.drawable.book)
            TypeLibraryObjects.Disk -> image.setImageResource(R.drawable.disk)
            TypeLibraryObjects.Newspaper -> image.setImageResource(R.drawable.newspaper)
        }
        name.text = "${item.name} с id: ${item.objectId}"

        name.alpha = if (item.access) 1f else 0.3f
        name.elevation = if (item.access) 10f else 1f
        image.elevation = if (item.access) 10f else 1f
    }
}