package com.library.activity.secondActivity

import androidx.recyclerview.widget.RecyclerView
import com.library.LibraryObjects
import com.library.databinding.ObjectActivityBinding

class ObjectViewHolder(private val binding: ObjectActivityBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: LibraryObjects, callButton: Boolean) = with(binding) {
        if (callButton){
            //
        } else {
            //
        }
    }
}