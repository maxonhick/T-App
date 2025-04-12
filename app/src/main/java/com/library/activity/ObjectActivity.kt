package com.library.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
import com.library.Month
import com.library.Newspaper
import com.library.R
import com.library.TypeLibraryObjects
import com.library.databinding.ObjectActivityBinding

class ObjectActivity : AppCompatActivity() {
    private lateinit var binding: ObjectActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ObjectActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isItemNew = intent.getBooleanExtra(IS_NEW, false)
        val type = intent.getStringExtra(TYPE_OBJECT)

        if (isItemNew) {
            setupForEditing(type!!)
        } else {
            setupForViewing(type!!)
        }

        binding.saveButton.setOnClickListener {
            val resultIntent: Intent
            if (isItemNew) {
                when (type) {
                    "Book" -> {
                        val newName = binding.editName.text.toString()
                        val newAuthor = binding.editAuthor.text.toString()
                        val newPages = Integer.parseInt(binding.editPages.text.toString())
                        val newId = Integer.parseInt(binding.editId.text.toString())
                        resultIntent = Intent().apply {
                            putExtra(IS_NEW, true)
                            putExtra(TYPE_OBJECT, TypeLibraryObjects.Book.name)
                            putExtra(BOOK_EXTRA, Book(
                                newId,
                                true,
                                newName,
                                TypeLibraryObjects.Book,
                                newPages,
                                newAuthor
                            ))
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                    "Disk" -> {
                        val newName = binding.editName.text.toString()
                        val newId = Integer.parseInt(binding.editId.text.toString())
                        val newTypeOfDisk = when (binding.diskTypeSpinner.selectedItemPosition) {
                            0 -> DiskType.CD
                            1 -> DiskType.DVD
                            else -> DiskType.CD
                        }
                        resultIntent = Intent().apply {
                            putExtra(IS_NEW, true)
                            putExtra(TYPE_OBJECT, TypeLibraryObjects.Disk.name)
                            putExtra(DISK_EXTRA, Disk(
                                objectId = newId,
                                access = true,
                                name = newName,
                                type =  newTypeOfDisk,
                                objectType = TypeLibraryObjects.Disk
                            ))
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                    "Newspaper" -> {
                        val newName = binding.editName.text.toString()
                        val newReleaseNumber = Integer.parseInt(binding.editReleaseNumber.text.toString())
                        val newId = Integer.parseInt(binding.editId.text.toString())
                        val newMonth = when (binding.monthNewspaper.selectedItemPosition) {
                            0 -> Month.January
                            1 -> Month.February
                            2 -> Month.March
                            3 -> Month.April
                            4 -> Month.May
                            5 -> Month.June
                            6 -> Month.July
                            7 -> Month.August
                            8 -> Month.September
                            9 -> Month.October
                            10 -> Month.November
                            11 -> Month.December
                            else -> Month.January
                        }
                        resultIntent = Intent().apply {
                            putExtra(IS_NEW, true)
                            putExtra(TYPE_OBJECT, TypeLibraryObjects.Newspaper.name)
                            putExtra(NEWSPAPER_EXTRA, Newspaper(
                                objectId = newId,
                                access = true,
                                name = newName,
                                releaseNumber = newReleaseNumber,
                                month = newMonth,
                                objectType = TypeLibraryObjects.Newspaper
                            ))
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }
            } else {
                resultIntent = Intent().apply {
                    putExtra(IS_NEW, false)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun setupForEditing(itemType: String) {
        binding.saveButton.text = "Сохранить"
        when (itemType) {
            "Book" -> {
                val item = intent.getParcelableExtra<Book>(BOOK_EXTRA)
                binding.typeLibraryObject.text = "Книга"
                binding.editName.isEnabled = true
                binding.editId.text = item?.objectId.toString()
                binding.editAuthor.visibility = View.VISIBLE
                binding.editAuthor.isEnabled = true
                binding.author.visibility = View.VISIBLE
                binding.valAccess.text = "Доступна"
                binding.month.visibility = View.GONE
                binding.monthNewspaper.visibility = View.GONE
                binding.pages.visibility = View.VISIBLE
                binding.editPages.visibility = View.VISIBLE
                binding.editPages.isEnabled = true
                binding.releaseNumber.visibility = View.GONE
                binding.editReleaseNumber.visibility = View.GONE
                binding.diskType.visibility = View.GONE
                binding.diskTypeSpinner.visibility = View.GONE
            }
            "Disk" -> {
                val item = intent.getParcelableExtra<Disk>(DISK_EXTRA)
                binding.typeLibraryObject.text = "Диск"
                binding.editName.isEnabled = true
                binding.editId.text = item?.objectId.toString()
                binding.editAuthor.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.valAccess.text = "Доступен"
                binding.month.visibility = View.GONE
                binding.monthNewspaper.visibility = View.GONE
                binding.pages.visibility = View.GONE
                binding.editPages.visibility = View.GONE
                binding.releaseNumber.visibility = View.GONE
                binding.editReleaseNumber.visibility = View.GONE
                binding.diskType.visibility = View.VISIBLE
                binding.diskTypeSpinner.visibility = View.VISIBLE
                binding.diskTypeSpinner.isEnabled = true
                val spinner: Spinner = binding.diskTypeSpinner
                ArrayAdapter.createFromResource(
                    this,
                    R.array.disk_type_array,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
            }
            "Newspaper" -> {
                val item = intent.getParcelableExtra<Newspaper>(NEWSPAPER_EXTRA)
                binding.typeLibraryObject.text = "Газета"
                binding.editName.isEnabled = true
                binding.editId.text = item?.objectId.toString()
                binding.editAuthor.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.valAccess.text = "Доступна"
                binding.month.visibility = View.VISIBLE
                binding.monthNewspaper.visibility = View.VISIBLE
                binding.pages.visibility = View.GONE
                binding.editPages.visibility = View.GONE
                binding.releaseNumber.visibility = View.VISIBLE
                binding.editReleaseNumber.visibility = View.VISIBLE
                binding.editReleaseNumber.isEnabled = true
                binding.diskType.visibility = View.GONE
                binding.diskTypeSpinner.visibility = View.GONE
                val spinner: Spinner = binding.monthNewspaper
                ArrayAdapter.createFromResource(
                    this,
                    R.array.month_array,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
            }
        }
    }

    private fun setupForViewing(type: String) {
        binding.saveButton.text = "Назад"
        when (type) {
            "Book" -> {
                val item = intent.getParcelableExtra<Book>(BOOK_EXTRA)
                binding.typeLibraryObject.text = "Книга"
                binding.editName.isEnabled = false
                binding.editName.setText(item?.name)
                binding.editId.setText(item?.objectId.toString())
                binding.editAuthor.visibility = View.VISIBLE
                binding.editAuthor.isEnabled = false
                binding.editAuthor.setText(item?.author)
                binding.author.visibility = View.VISIBLE
                binding.valAccess.text = item?.access.toString()
                binding.month.visibility = View.GONE
                binding.monthNewspaper.visibility = View.GONE
                binding.pages.visibility = View.VISIBLE
                binding.editPages.visibility = View.VISIBLE
                binding.editPages.isEnabled = false
                binding.editPages.setText(item?.pages.toString())
                binding.releaseNumber.visibility = View.GONE
                binding.editReleaseNumber.visibility = View.GONE
                binding.diskType.visibility = View.GONE
                binding.diskTypeSpinner.visibility = View.GONE
            }
            "Disk" -> {
                val item = intent.getParcelableExtra<Disk>(DISK_EXTRA)
                binding.typeLibraryObject.text = "Диск"
                binding.editName.isEnabled = false
                binding.editName.setText(item?.name)
                binding.editId.setText(item?.objectId.toString())
                binding.editAuthor.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.valAccess.text = item?.access.toString()
                binding.month.visibility = View.GONE
                binding.monthNewspaper.visibility = View.GONE
                binding.pages.visibility = View.GONE
                binding.editPages.visibility = View.GONE
                binding.releaseNumber.visibility = View.GONE
                binding.editReleaseNumber.visibility = View.GONE
                binding.diskType.visibility = View.VISIBLE
                binding.diskTypeSpinner.visibility = View.VISIBLE
                binding.diskTypeSpinner.isEnabled = false
                binding.diskTypeSpinner.setSelection(when (item?.type){
                    DiskType.DVD -> 1
                    DiskType.CD -> 0
                    else -> 1
                })
            }
            "Newspaper" -> {
                val item = intent.getParcelableExtra<Newspaper>(NEWSPAPER_EXTRA)
                binding.typeLibraryObject.text = "Газета"
                binding.editName.isEnabled = false
                binding.editName.setText(item?.name)
                binding.editId.setText(item?.objectId.toString())
                binding.editAuthor.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.valAccess.text = item?.access.toString()
                binding.month.visibility = View.VISIBLE
                binding.monthNewspaper.visibility = View.VISIBLE
                binding.diskTypeSpinner.isEnabled = false
                binding.diskTypeSpinner.setSelection(when (item?.month){
                    Month.January-> 0
                    Month.February -> 1
                    Month.March -> 2
                    Month.April -> 3
                    Month.May -> 4
                    Month.June -> 5
                    Month.July -> 6
                    Month.August -> 7
                    Month.September -> 8
                    Month.October -> 9
                    Month.November -> 10
                    Month.December -> 11
                    else -> 0
                })
                binding.pages.visibility = View.GONE
                binding.editPages.visibility = View.GONE
                binding.releaseNumber.visibility = View.VISIBLE
                binding.editReleaseNumber.visibility = View.VISIBLE
                binding.editReleaseNumber.isEnabled = false
                binding.editReleaseNumber.setText(item?.releaseNumber.toString())
                binding.diskType.visibility = View.GONE
                binding.diskTypeSpinner.visibility = View.GONE
            }
        }
    }

    companion object {
        const val IS_NEW = "IS_NEW"
        const val TYPE_OBJECT = "TYPEOBJECT"
        const val BOOK_EXTRA = "BOOK_EXTRA"
        const val DISK_EXTRA = "DISK_EXTRA"
        const val NEWSPAPER_EXTRA = "NEWSPAPER_EXTRA"

        fun createIntent(
            context: Context,
            isNew: Boolean = false,
            item: LibraryObjects
        ): Intent {
            return Intent(context, ObjectActivity::class.java).apply {
                putExtra(IS_NEW, isNew)
                putExtra(TYPE_OBJECT, item.objectType.name)
                when (item.objectType) {
                    TypeLibraryObjects.Book -> putExtra(BOOK_EXTRA, item as Book)
                    TypeLibraryObjects.Disk -> putExtra(DISK_EXTRA, item as Disk)
                    TypeLibraryObjects.Newspaper -> putExtra(NEWSPAPER_EXTRA, item as Newspaper)
                }
            }
        }
    }
}