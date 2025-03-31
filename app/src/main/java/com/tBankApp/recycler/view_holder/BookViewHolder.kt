package com.tBankApp.recycler.view_holder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.library.Book
import com.library.R
import com.library.databinding.BookItemBinding

class BookViewHolder(private val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(book: Book) = with(binding) {
        image.setImageResource(R.drawable.book)
        name.text = "${book.name} с id: ${book.objectId}"

        name.alpha = if (book.access) 1f else 0.3f
        name.elevation = if (book.access) 10f else 1f
        image.elevation = if (book.access) 10f else 1f
    }
}