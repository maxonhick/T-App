package com.library.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.TypeLibraryObjects
import com.library.activity.ObjectActivity.Companion.IS_NEW
import com.library.activity.ObjectActivity.Companion.ITEM_ID
import com.library.activity.ObjectActivity.Companion.TYPE_OBJECT
import com.library.databinding.ActivityMainBinding
import com.tBankApp.recycler.LibraryAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var libraryAdapter: LibraryAdapter
    private lateinit var viewModel: LibraryViewModel

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val isNew = result.data?.getBooleanExtra(IS_NEW, false)
            val itemId = result.data?.getIntExtra(ITEM_ID, -1)

            viewModel.items.observe(this) { items ->
                libraryAdapter.submitList(items)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        libraryAdapter = LibraryAdapter { item ->
            openSecondActivity(item.objectId, false, null)
        }
        with(binding.recyclerView){
            layoutManager = LinearLayoutManager(context)
            adapter = libraryAdapter
        }

        binding.addBook.setOnClickListener {
            openSecondActivity(null, true, TypeLibraryObjects.Book)
        }

        binding.addDisk.setOnClickListener {
            openSecondActivity(null, true, TypeLibraryObjects.Disk)
        }

        binding.addNewspaper.setOnClickListener {
            openSecondActivity(null, true, TypeLibraryObjects.Newspaper)
        }

        initViewModel()
    }

    fun openSecondActivity(itemId: Int?, isNew: Boolean, type: TypeLibraryObjects?) {
        val intent = Intent(this, ObjectActivity::class.java).apply {
            putExtra(ITEM_ID, itemId ?: -1)
            putExtra(IS_NEW, isNew)
            type?.let {
                putExtra(TYPE_OBJECT, it.name)
            }
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startForResult.launch(intent)
    }

    private fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[LibraryViewModel::class.java]

        viewModel.items.observe(this){items ->
            libraryAdapter.submitList(items)
        }
    }
}