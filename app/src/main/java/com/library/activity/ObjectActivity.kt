package com.library.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
import com.library.Month
import com.library.Newspaper
import com.library.R
import com.library.TypeLibraryObjects
import com.library.databinding.ObjectActivityBinding

class ObjectActivity : Activity() {
    private lateinit var binding: ObjectActivityBinding
    private lateinit var currentItem: LibraryObjects
    private var isItemNew: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ObjectActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isItemNew = intent.getBooleanExtra(IS_NEW, false)
        val itemId = intent.getIntExtra(ITEM_ID, -1)
        val type = intent.getStringExtra(TYPE_OBJECT)

        if (isItemNew) {
            when (type) {
                "Book" -> currentItem = Book(0, false, "", TypeLibraryObjects.Book, 0, "")
                "Disk" -> currentItem = Disk(0, false, "", DiskType.CD, TypeLibraryObjects.Disk)
                "Newspaper" -> currentItem = Newspaper(0, false,"", 0, Month.January, TypeLibraryObjects.Newspaper)
                else -> finish()
            }
            setupForEditing(currentItem)
        } else {
            currentItem = try {
                LibraryViewModel().getItemById(itemId)
            } catch (e: Exception){
                finish()
                return
            }
            setupForViewing()
        }

        binding.saveButton.setOnClickListener {
            val resultIntent: Intent
            if (isItemNew) {
                when (currentItem.objectType) {
                    TypeLibraryObjects.Book -> {
                        val newName = binding.editName.text.toString()
                        val newAuthor = binding.editAuthor.text.toString()
                        val newPages = Integer.parseInt(binding.editPages.text.toString())
                        val newId = Integer.parseInt(binding.editId.text.toString())
                        LibraryViewModel().addNewItem(listOf(Book(newId, true, newName, TypeLibraryObjects.Book, newPages, newAuthor)))
                        resultIntent = Intent().apply {
                            putExtra(ITEM_ID, newId)
                            putExtra(IS_NEW, true)
                        }
                    }
                    TypeLibraryObjects.Disk -> {
                        val newName = binding.editName.text.toString()
                        val newId = Integer.parseInt(binding.editId.text.toString())
                        val newTypeOfDisk = when (binding.diskTypeSpinner.selectedItemPosition) {
                            0 -> DiskType.CD
                            1 -> DiskType.DVD
                            else -> DiskType.CD
                        }
                        LibraryViewModel().addNewItem(listOf(Disk(newId, true, newName, newTypeOfDisk, TypeLibraryObjects.Disk)))
                        resultIntent = Intent().apply {
                            putExtra(ITEM_ID, newId)
                            putExtra(IS_NEW, true)
                        }
                    }
                    TypeLibraryObjects.Newspaper -> {
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
                        LibraryViewModel().addNewItem(listOf(Newspaper(newId, true, newName, newReleaseNumber, newMonth, TypeLibraryObjects.Newspaper)))
                        resultIntent = Intent().apply {
                            putExtra(ITEM_ID, newId)
                            putExtra(IS_NEW, true)
                        }
                    }
                }
            } else {
                resultIntent = Intent().apply {
                    putExtra(ITEM_ID, -1)
                    putExtra(IS_NEW, false)
                }
            }

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun setupForEditing(item: LibraryObjects) {
        binding.saveButton.text = "Сохранить"
        when (item.objectType) {
            TypeLibraryObjects.Book -> {
                binding.typeLibraryObject.text = "Книга"
                binding.editName.isEnabled = true
                binding.editId.text = ((LibraryViewModel().items.value?.size ?: 0) + 1).toString()
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
            TypeLibraryObjects.Disk -> {
                binding.typeLibraryObject.text = "Диск"
                binding.editName.isEnabled = true
                binding.editId.text = ((LibraryViewModel().items.value?.size ?: 0) + 1).toString()
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
            TypeLibraryObjects.Newspaper -> {
                binding.typeLibraryObject.text = "Газета"
                binding.editName.isEnabled = true
                binding.editId.text = ((LibraryViewModel().items.value?.size ?: 0) + 1).toString()
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

//    private fun loadItem(itemId: Int): LibraryObjects {
//        return LibraryViewModel().getItemById(itemId) ?: throw IllegalArgumentException("Нет такого ID")
//    }

    private fun setupForViewing() {
        binding.saveButton.text = "Назад"
        when (currentItem.objectType) {
            TypeLibraryObjects.Book -> {
                binding.typeLibraryObject.text = "Книга"
                binding.editName.isEnabled = false
                binding.editName.setText(currentItem.name)
                binding.editId.setText(currentItem.objectId.toString())
                binding.editAuthor.visibility = View.VISIBLE
                binding.editAuthor.isEnabled = false
                binding.editAuthor.setText((currentItem as Book).author)
                binding.author.visibility = View.VISIBLE
                binding.valAccess.text = currentItem.access.toString()
                binding.month.visibility = View.GONE
                binding.monthNewspaper.visibility = View.GONE
                binding.pages.visibility = View.VISIBLE
                binding.editPages.visibility = View.VISIBLE
                binding.editPages.isEnabled = false
                binding.editPages.setText((currentItem as Book).pages.toString())
                binding.releaseNumber.visibility = View.GONE
                binding.editReleaseNumber.visibility = View.GONE
                binding.diskType.visibility = View.GONE
                binding.diskTypeSpinner.visibility = View.GONE
            }
            TypeLibraryObjects.Disk -> {
                binding.typeLibraryObject.text = "Диск"
                binding.editName.isEnabled = false
                binding.editName.setText(currentItem.name)
                binding.editId.setText(currentItem.objectId.toString())
                binding.editAuthor.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.valAccess.text = currentItem.access.toString()
                binding.month.visibility = View.GONE
                binding.monthNewspaper.visibility = View.GONE
                binding.pages.visibility = View.GONE
                binding.editPages.visibility = View.GONE
                binding.releaseNumber.visibility = View.GONE
                binding.editReleaseNumber.visibility = View.GONE
                binding.diskType.visibility = View.VISIBLE
                binding.diskTypeSpinner.visibility = View.VISIBLE
                binding.diskTypeSpinner.isEnabled = false
                binding.diskTypeSpinner.setSelection(when ((currentItem as Disk).type){
                    DiskType.DVD -> 1
                    DiskType.CD -> 0
                })
            }
            TypeLibraryObjects.Newspaper -> {
                binding.typeLibraryObject.text = "Газета"
                binding.editName.isEnabled = false
                binding.editName.setText(currentItem.name)
                binding.editId.setText(currentItem.objectId.toString())
                binding.editAuthor.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.valAccess.text = currentItem.access.toString()
                binding.month.visibility = View.VISIBLE
                binding.monthNewspaper.visibility = View.VISIBLE
                binding.diskTypeSpinner.isEnabled = false
                binding.diskTypeSpinner.setSelection(when ((currentItem as Newspaper).month){
                    Month.January -> 0
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
                })
                binding.pages.visibility = View.GONE
                binding.editPages.visibility = View.GONE
                binding.releaseNumber.visibility = View.VISIBLE
                binding.editReleaseNumber.visibility = View.VISIBLE
                binding.editReleaseNumber.isEnabled = false
                binding.editReleaseNumber.setText((currentItem as Newspaper).releaseNumber.toString())
                binding.diskType.visibility = View.GONE
                binding.diskTypeSpinner.visibility = View.GONE
            }
        }
    }

    companion object {
        const val ITEM_ID = "ITEM_ID"
        const val IS_NEW = "IS_NEW"
        const val TYPE_OBJECT = "TYPEOBJECT"

        fun createIntent(
            context: Context,
            itemId: Int? = null,
            isNew: Boolean = false,
            type: TypeLibraryObjects? = null
        ): Intent {
            return Intent(context, ObjectActivity::class.java).apply {
                putExtra(ITEM_ID, itemId ?: -1)
                putExtra(IS_NEW, isNew)
                type?.let { putExtra(TYPE_OBJECT, it.toString()) }
            }
        }
    }
}