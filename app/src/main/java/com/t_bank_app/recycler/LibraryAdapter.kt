package com.t_bank_app.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.t_bank_app.R
import com.t_bank_app.databinding.BookItemBinding
import com.t_bank_app.databinding.DiskItemBinding
import com.t_bank_app.databinding.NewspaperItemBinding
import com.t_bank_app.library.Book
import com.t_bank_app.library.Disk
import com.t_bank_app.library.DiskType
import com.t_bank_app.library.Month
import com.t_bank_app.library.Newspaper
import com.t_bank_app.recycler.view_holder.BookViewHolder
import com.t_bank_app.recycler.view_holder.DiskViewHolder
import com.t_bank_app.recycler.view_holder.NewspaperViewHolder

class LibraryAdapter: RecyclerView.Adapter<ViewHolder>() {
    private val data = listOf(
        Book(objectId = 1289, access = true, name = "Маугли", pages = 100, author = "Киплинг"),
        Book(objectId = 3609, access = true, name = "Чёрный обелиск", pages = 479, author = "Ремарк"),
        Book(objectId = 6372, access = true, name = "1984", pages = 400, author = "Оруэл"),
        Book(objectId = 3876, access = true, name = "Война и мир", pages = 1472, author = "Толстой"),
        Newspaper(objectId =  6341, access = true, name = "WSJ", releaseNumber = 120225, month = Month.January),
        Newspaper(objectId =  6371, access = true, name = "Зеленоград.ru", releaseNumber = 121124, month = Month.March),
        Newspaper(objectId =  6383, access = true, name = "Спорт-Экпресс", releaseNumber = 230125, month = Month.October),
        Newspaper(objectId =  6342, access = true, name = "WSJ", releaseNumber = 200225, month = Month.June),
        Newspaper(objectId =  6392, access = true, name = "Коммерсантъ", releaseNumber = 130325, month = Month.July),
        Disk(objectId = 9234, access = true, name = "Назад в будущее", type = DiskType.DVD),
        Disk(objectId = 9245, access = true, name = "Довод", type = DiskType.CD),
        Disk(objectId = 9239, access = true, name = "Дивергент", type = DiskType.CD),
        Disk(objectId = 9296, access = true, name = "Рио", type = DiskType.DVD),
        Disk(objectId = 9212, access = true, name = "Люди в чёрном", type = DiskType.DVD)
    )

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return when(item) {
            is Book -> R.layout.book_item
            is Newspaper -> R.layout.newspaper_item
            is Disk -> R.layout.disk_item
            else -> throw IllegalArgumentException("Unknown element: $item")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.book_item -> {
                val binding = BookItemBinding.inflate(inflater)
                BookViewHolder(binding).apply {
                    binding.root.setOnClickListener {
                        handlePersonClick(binding.root.context, adapterPosition)
                    }
                }
            }
            R.layout.disk_item -> {
                val binding = DiskItemBinding.inflate(inflater)
                DiskViewHolder(binding).apply {
                    binding.root.setOnClickListener {
                        handlePersonClick(binding.root.context, adapterPosition)
                    }
                }
            }
            R.layout.newspaper_item -> {
                val binding = NewspaperItemBinding.inflate(inflater)
                NewspaperViewHolder(binding).apply {
                    binding.root.setOnClickListener {
                        handlePersonClick(binding.root.context, adapterPosition)
                    }
                }
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is BookViewHolder -> holder.bind(item as Book)
            is DiskViewHolder -> holder.bind(item as Disk)
            is NewspaperViewHolder -> holder.bind(item as Newspaper)
        }
    }

    override fun getItemCount() = data.size

    private fun handlePersonClick(context: Context, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = data[position]
            makeText(context, "Элемент с id ${item.objectId}", LENGTH_SHORT).show()
            item.access = !(item.access)
            notifyItemChanged(position)
        }
    }
}