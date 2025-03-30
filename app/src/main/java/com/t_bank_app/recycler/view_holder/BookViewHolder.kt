package com.t_bank_app.recycler.view_holder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.t_bank_app.R
import com.t_bank_app.databinding.BookItemBinding
import com.t_bank_app.library.Book

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