package com.library.activity

import android.content.Intent
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
import com.library.activity.ObjectActivity.Companion.ACCESS
import com.library.activity.ObjectActivity.Companion.AUTHOR
import com.library.activity.ObjectActivity.Companion.DISK_TYPE
import com.library.activity.ObjectActivity.Companion.IS_NEW
import com.library.activity.ObjectActivity.Companion.ITEM_ID
import com.library.activity.ObjectActivity.Companion.MONTH
import com.library.activity.ObjectActivity.Companion.NAME
import com.library.activity.ObjectActivity.Companion.PAGES
import com.library.activity.ObjectActivity.Companion.RELEASE
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
            val type = result.data?.getStringExtra(TYPE_OBJECT)

            if (isNew == true) {
                when (type){
                    "Book" -> {
                        viewModel.addNewItem(listOf(
                            Book(
                                result.data?.getIntExtra(ITEM_ID, 0)!!,
                                result.data?.getBooleanExtra(ACCESS, true)!!,
                                result.data?.getStringExtra(NAME)!!,
                                TypeLibraryObjects.Book,
                                result.data?.getIntExtra(PAGES, 0)!!,
                                result.data?.getStringExtra(AUTHOR)!!
                                )
                        ))
                    }
                    "Disk" -> {
                        viewModel.addNewItem(listOf(
                            Disk(
                                result.data?.getIntExtra(ITEM_ID, 0)!!,
                                result.data?.getBooleanExtra(ACCESS, true)!!,
                                result.data?.getStringExtra(NAME)!!,
                                when (result.data?.getStringExtra(DISK_TYPE)!!) {
                                    "CD" -> DiskType.CD
                                    "DVD" -> DiskType.DVD
                                    else -> DiskType.CD
                                },
                                TypeLibraryObjects.Disk
                            )
                        ))
                    }
                    "Newspaper" -> {
                        viewModel.addNewItem(listOf(
                            Newspaper(
                                result.data?.getIntExtra(ITEM_ID, 0)!!,
                                result.data?.getBooleanExtra(ACCESS, true)!!,
                                result.data?.getStringExtra(NAME)!!,
                                result.data?.getIntExtra(RELEASE, 0)!!,
                                when (result.data?.getStringExtra(MONTH)){
                                    "January" -> Month.January
                                    "February" -> Month.February
                                    "March" -> Month.March
                                    "April" -> Month.April
                                    "May" -> Month.May
                                    "June" -> Month.June
                                    "July" -> Month.July
                                    "August" -> Month.August
                                    "September" -> Month.September
                                    "October" -> Month.October
                                    "November" -> Month.November
                                    "December" -> Month.December
                                    else -> Month.January
                                },
                                TypeLibraryObjects.Newspaper
                            )
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
        val intent = Intent(this, ObjectActivity::class.java).apply {
            putExtra(ITEM_ID, item.objectId)
            putExtra(IS_NEW, isNew)
            putExtra(TYPE_OBJECT, item.objectType.name)

            if (!isNew) {
                putExtra(NAME, item.name)
                putExtra(ACCESS, item.access)
                when (item.objectType) {
                    TypeLibraryObjects.Book -> {
                        putExtra(AUTHOR, (item as Book).author)
                        putExtra(PAGES, item.pages)
                    }
                    TypeLibraryObjects.Disk -> putExtra(DISK_TYPE, (item as Disk).type.name)
                    TypeLibraryObjects.Newspaper -> {
                        putExtra(RELEASE, (item as Newspaper).releaseNumber)
                        putExtra(MONTH, item.month.name)
                    }
                    null -> null
                }
            }
        }

        startForResult.launch(intent)
    }

    private fun initViewModel() {
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[LibraryViewModel::class.java]

        viewModel.items.observe(this){items ->
            libraryAdapter.submitList(items.toList())
        }
        if (viewModel.items.value.isNullOrEmpty()) {
            viewModel.addNewItem(
                listOf(
                    Book(
                        objectId = 1,
                        access = true,
                        name = "Маугли",
                        pages = 100,
                        author = "Киплинг",
                        objectType = TypeLibraryObjects.Book
                    ),
                    Book(
                        objectId = 2,
                        access = true,
                        name = "Чёрный обелиск",
                        pages = 479,
                        author = "Ремарк",
                        objectType = TypeLibraryObjects.Book
                    ),
                    Book(
                        objectId = 3, access = true, name = "1984", pages = 400, author = "Оруэл",
                        objectType = TypeLibraryObjects.Book
                    ),
                    Book(
                        objectId = 4,
                        access = true,
                        name = "Война и мир",
                        pages = 1472,
                        author = "Толстой",
                        objectType = TypeLibraryObjects.Book
                    ),
                    Newspaper(
                        objectId = 5,
                        access = true,
                        name = "WSJ",
                        releaseNumber = 120225,
                        month = Month.January,
                        objectType = TypeLibraryObjects.Newspaper
                    ),
                    Newspaper(
                        objectId = 6,
                        access = true,
                        name = "Зеленоград.ru",
                        releaseNumber = 121124,
                        month = Month.March,
                        objectType = TypeLibraryObjects.Newspaper
                    ),
                    Newspaper(
                        objectId = 7,
                        access = true,
                        name = "Спорт-Экпресс",
                        releaseNumber = 230125,
                        month = Month.October,
                        objectType = TypeLibraryObjects.Newspaper
                    ),
                    Newspaper(
                        objectId = 8,
                        access = true,
                        name = "WSJ",
                        releaseNumber = 200225,
                        month = Month.June,
                        objectType = TypeLibraryObjects.Newspaper
                    ),
                    Newspaper(
                        objectId = 9,
                        access = true,
                        name = "Коммерсантъ",
                        releaseNumber = 130325,
                        month = Month.July,
                        objectType = TypeLibraryObjects.Newspaper
                    ),
                    Disk(
                        objectId = 10, access = true, name = "Назад в будущее", type = DiskType.DVD,
                        objectType = TypeLibraryObjects.Disk
                    ),
                    Disk(
                        objectId = 11, access = true, name = "Довод", type = DiskType.CD,
                        objectType = TypeLibraryObjects.Disk
                    ),
                    Disk(
                        objectId = 12, access = true, name = "Дивергент", type = DiskType.CD,
                        objectType = TypeLibraryObjects.Disk
                    ),
                    Disk(
                        objectId = 13, access = true, name = "Рио", type = DiskType.DVD,
                        objectType = TypeLibraryObjects.Disk
                    ),
                    Disk(
                        objectId = 14, access = true, name = "Люди в чёрном", type = DiskType.DVD,
                        objectType = TypeLibraryObjects.Disk
                    )
                )
            )
        }
    }
}