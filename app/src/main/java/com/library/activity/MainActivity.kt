package com.library.activity

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
import com.library.Month
import com.library.Newspaper
import com.library.TypeLibraryObjects
import com.library.activity.ObjectActivity.Companion.BOOK_EXTRA
import com.library.activity.ObjectActivity.Companion.DISK_EXTRA
import com.library.activity.ObjectActivity.Companion.IS_NEW
import com.library.activity.ObjectActivity.Companion.NEWSPAPER_EXTRA
import com.library.activity.ObjectActivity.Companion.TYPE_OBJECT
import com.library.activity.ObjectActivity.Companion.createIntent
import com.library.databinding.ActivityMainBinding
import com.tBankApp.recycler.LibraryAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var libraryAdapter: LibraryAdapter
    private lateinit var viewModel: LibraryViewModel

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val isNew = result.data?.getBooleanExtra(IS_NEW, false)

            if (isNew == true) {
                val type = result.data?.getStringExtra(TYPE_OBJECT)
                when (type){
                    "Book" -> {
                        viewModel.addNewItem(listOf(
                            result.data?.getParcelableExtra<Book>(BOOK_EXTRA)!!
                        ))
                    }
                    "Disk" -> {
                        viewModel.addNewItem(listOf(
                            result.data?.getParcelableExtra<Disk>(DISK_EXTRA)!!
                        ))
                    }
                    "Newspaper" -> {
                        viewModel.addNewItem(listOf(
                            result.data?.getParcelableExtra<Newspaper>(NEWSPAPER_EXTRA)!!
                        ))
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        libraryAdapter = LibraryAdapter { item ->
            openSecondActivity(false, item)
        }
        with(binding.recyclerView){
            layoutManager = LinearLayoutManager(context)
            adapter = libraryAdapter
        }

        binding.addBook.setOnClickListener {
            openSecondActivity(true, Book(viewModel.getSize() + 1, false, "", TypeLibraryObjects.Book, 0, ""))
        }

        binding.addDisk.setOnClickListener {
            openSecondActivity(true, Disk(viewModel.getSize() + 1, false, "", DiskType.CD, TypeLibraryObjects.Disk))
        }

        binding.addNewspaper.setOnClickListener {
            openSecondActivity(true,Newspaper(viewModel.getSize() + 1, false, "", 0, Month.July, TypeLibraryObjects.Newspaper) )
        }

        initViewModel()
    }

    private fun openSecondActivity(isNew: Boolean, item: LibraryObjects) {
        val intent = createIntent(this, isNew, item)

        startForResult.launch(intent)
    }

    private fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[LibraryViewModel::class.java]

        viewModel.items.observe(this){items ->
            libraryAdapter.submitList(items.toList())
        }
    }
}