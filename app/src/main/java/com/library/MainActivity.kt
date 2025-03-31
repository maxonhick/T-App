package com.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.databinding.ActivityMainBinding
import com.tBankApp.recycler.LibraryAdapter

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