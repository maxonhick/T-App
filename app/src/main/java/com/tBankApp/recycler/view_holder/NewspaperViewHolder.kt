package com.tBankApp.recycler.view_holder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.library.Newspaper
import com.library.R
import com.library.databinding.NewspaperItemBinding

class NewspaperViewHolder(private val binding: NewspaperItemBinding): RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(newspaper: Newspaper) = with(binding) {
        image.setImageResource(R.drawable.newspaper)
        name.text = "${newspaper.name} с id: ${newspaper.objectId}"

        name.alpha = if (newspaper.access) 1f else 0.3f
        name.elevation = if (newspaper.access) 10f else 1f
        image.elevation = if (newspaper.access) 10f else 1f
    }
}