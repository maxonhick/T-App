package com.library.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.library.R
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
                            putExtra(TYPE_OBJECT, "Book")
                            putExtra(ITEM_ID, newId)
                            putExtra(IS_NEW, true)
                            putExtra(NAME, newName)
                            putExtra(ACCESS, true)
                            putExtra(AUTHOR, newAuthor)
                            putExtra(PAGES, newPages)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                    "Disk" -> {
                        val newName = binding.editName.text.toString()
                        val newId = Integer.parseInt(binding.editId.text.toString())
                        val newTypeOfDisk = when (binding.diskTypeSpinner.selectedItemPosition) {
                            0 -> "CD"
                            1 -> "DVD"
                            else -> "CD"
                        }
                        resultIntent = Intent().apply {
                            putExtra(TYPE_OBJECT, "Disk")
                            putExtra(ITEM_ID, newId)
                            putExtra(IS_NEW, true)
                            putExtra(NAME, newName)
                            putExtra(ACCESS, true)
                            putExtra(DISK_TYPE, newTypeOfDisk)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                    "Newspaper" -> {
                        val newName = binding.editName.text.toString()
                        val newReleaseNumber = Integer.parseInt(binding.editReleaseNumber.text.toString())
                        val newId = Integer.parseInt(binding.editId.text.toString())
                        val newMonth = when (binding.monthNewspaper.selectedItemPosition) {
                            0 -> "January"
                            1 -> "February"
                            2 -> "March"
                            3 -> "April"
                            4 -> "May"
                            5 -> "June"
                            6 -> "July"
                            7 -> "August"
                            8 -> "September"
                            9 -> "October"
                            10 -> "November"
                            11 -> "December"
                            else -> "January"
                        }
                        resultIntent = Intent().apply {
                            putExtra(TYPE_OBJECT, "Newspaper")
                            putExtra(ITEM_ID, newId)
                            putExtra(IS_NEW, true)
                            putExtra(NAME, newName)
                            putExtra(ACCESS, true)
                            putExtra(RELEASE, newReleaseNumber)
                            putExtra(MONTH, newMonth)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }
            } else {
                resultIntent = Intent().apply {
                    putExtra(ITEM_ID, -1)
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
                binding.typeLibraryObject.text = "Книга"
                binding.editName.isEnabled = true
                binding.editId.text = intent.getIntExtra(ITEM_ID, 1).toString()
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
                binding.typeLibraryObject.text = "Диск"
                binding.editName.isEnabled = true
                binding.editId.text = intent.getIntExtra(ITEM_ID, 1).toString()
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
                binding.typeLibraryObject.text = "Газета"
                binding.editName.isEnabled = true
                binding.editId.text = intent.getIntExtra(ITEM_ID, 1).toString()
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
                binding.typeLibraryObject.text = "Книга"
                binding.editName.isEnabled = false
                binding.editName.setText(intent.getStringExtra(NAME))
                binding.editId.setText(intent.getIntExtra(ITEM_ID, 1).toString())
                binding.editAuthor.visibility = View.VISIBLE
                binding.editAuthor.isEnabled = false
                binding.editAuthor.setText(intent.getStringExtra(AUTHOR))
                binding.author.visibility = View.VISIBLE
                binding.valAccess.text = intent.getBooleanExtra(ACCESS, true).toString()
                binding.month.visibility = View.GONE
                binding.monthNewspaper.visibility = View.GONE
                binding.pages.visibility = View.VISIBLE
                binding.editPages.visibility = View.VISIBLE
                binding.editPages.isEnabled = false
                binding.editPages.setText(intent.getIntExtra(PAGES, 0).toString())
                binding.releaseNumber.visibility = View.GONE
                binding.editReleaseNumber.visibility = View.GONE
                binding.diskType.visibility = View.GONE
                binding.diskTypeSpinner.visibility = View.GONE
            }
            "Disk" -> {
                binding.typeLibraryObject.text = "Диск"
                binding.editName.isEnabled = false
                binding.editName.setText(intent.getStringExtra(NAME))
                binding.editId.setText(intent.getIntExtra(ITEM_ID, 1).toString())
                binding.editAuthor.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.valAccess.text = intent.getBooleanExtra(ACCESS, true).toString()
                binding.month.visibility = View.GONE
                binding.monthNewspaper.visibility = View.GONE
                binding.pages.visibility = View.GONE
                binding.editPages.visibility = View.GONE
                binding.releaseNumber.visibility = View.GONE
                binding.editReleaseNumber.visibility = View.GONE
                binding.diskType.visibility = View.VISIBLE
                binding.diskTypeSpinner.visibility = View.VISIBLE
                binding.diskTypeSpinner.isEnabled = false
                binding.diskTypeSpinner.setSelection(when (intent.getStringExtra(DISK_TYPE)){
                    "DVD" -> 1
                    "CD" -> 0
                    else -> 1
                })
            }
            "Newspaper" -> {
                binding.typeLibraryObject.text = "Газета"
                binding.editName.isEnabled = false
                binding.editName.setText(intent.getStringExtra(NAME))
                binding.editId.setText(intent.getIntExtra(ITEM_ID, 1).toString())
                binding.editAuthor.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.valAccess.text = intent.getBooleanExtra(ACCESS, true).toString()
                binding.month.visibility = View.VISIBLE
                binding.monthNewspaper.visibility = View.VISIBLE
                binding.diskTypeSpinner.isEnabled = false
                binding.diskTypeSpinner.setSelection(when (intent.getStringExtra(MONTH)){
                    "January" -> 0
                    "February" -> 1
                    "March" -> 2
                    "April" -> 3
                    "May" -> 4
                    "June" -> 5
                    "July" -> 6
                    "August" -> 7
                    "September" -> 8
                    "October" -> 9
                    "November" -> 10
                    "December" -> 11
                    else -> 0
                })
                binding.pages.visibility = View.GONE
                binding.editPages.visibility = View.GONE
                binding.releaseNumber.visibility = View.VISIBLE
                binding.editReleaseNumber.visibility = View.VISIBLE
                binding.editReleaseNumber.isEnabled = false
                binding.editReleaseNumber.setText(intent.getIntExtra(RELEASE, 0).toString())
                binding.diskType.visibility = View.GONE
                binding.diskTypeSpinner.visibility = View.GONE
            }
        }
    }

    companion object {
        const val ITEM_ID = "ITEM_ID"
        const val IS_NEW = "IS_NEW"
        const val TYPE_OBJECT = "TYPEOBJECT"
        const val ACCESS = "ACCESS"
        const val NAME = "NAME"
        const val PAGES = "PAGES"
        const val AUTHOR = "AUTHOR"
        const val DISK_TYPE = "DISK_TYPE"
        const val  RELEASE = "RELEASE"
        const val MONTH = "MONTH"
    }
}