package com.t_bank_app.recycler.view_holder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.t_bank_app.R
import com.t_bank_app.databinding.DiskItemBinding
import com.t_bank_app.library.Disk

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