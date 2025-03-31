package com.tBankApp.recycler.view_holder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.library.Disk
import com.library.R
import com.library.databinding.DiskItemBinding

class DiskViewHolder(private val binding: DiskItemBinding): RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(disk: Disk) = with(binding) {
        image.setImageResource(R.drawable.disk)
        name.text = "${disk.name} с id: ${disk.objectId}"

        name.alpha = if (disk.access) 1f else 0.3f
        name.elevation = if (disk.access) 10f else 1f
        image.elevation = if (disk.access) 10f else 1f
    }
}