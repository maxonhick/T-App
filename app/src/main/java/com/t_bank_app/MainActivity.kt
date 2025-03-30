package com.t_bank_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.t_bank_app.databinding.ActivityMainBinding
import com.t_bank_app.recycler.LibraryAdapter

class MainActivity : AppCompatActivity() {

    private val libraryAdapter = LibraryAdapter()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding.recyclerView){
            layoutManager = LinearLayoutManager(context)
            adapter = libraryAdapter
        }
    }
}