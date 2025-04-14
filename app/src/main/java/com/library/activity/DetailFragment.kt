package com.library.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
import com.library.Month
import com.library.Newspaper
import com.library.R
import com.library.TypeLibraryObjects
import com.library.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private var isNew: Boolean = false
    private lateinit var item: LibraryObjects
    private lateinit var typeObject: TypeLibraryObjects
    private val viewModel: LibraryViewModel by activityViewModels()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        arguments?.let {
//            isNew = it.getBoolean(IS_NEW)
//            typeObject = TypeLibraryObjects.valueOf(it.getString(TYPE_OBJECT)!!)
//            when (typeObject) {
//                TypeLibraryObjects.Book -> item = it.getParcelable<Book>(BOOK_BUNDLE)
//                TypeLibraryObjects.Disk -> item = it.getParcelable<Disk>(DISK_BUNDLE)
//                TypeLibraryObjects.Newspaper -> item = it.getParcelable<Newspaper>(NEWSPAPER_BUNDLE)
//                null -> null
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            isNew = it.getBoolean(IS_NEW)
            typeObject = TypeLibraryObjects.valueOf(it.getString(TYPE_OBJECT)!!)
            item = when (typeObject) {
                TypeLibraryObjects.Book -> it.getParcelable(BOOK_BUNDLE)!!
                TypeLibraryObjects.Disk -> it.getParcelable(DISK_BUNDLE)!!
                TypeLibraryObjects.Newspaper -> it.getParcelable(NEWSPAPER_BUNDLE)!!
            }
        }

        if (isNew) {
            setupForEditing()
        } else {
            setupForViewing()
        }

        binding.saveButton.setOnClickListener {
            if (isNew) {
                saveNewItem()
            } else {
                (activity as MainActivity).closeDetailFragment()
            }
        }
    }

    private fun setupForEditing() {
        binding.saveButton.text = "Сохранить"
        when (item.objectType) {
            TypeLibraryObjects.Book -> setupBookEditing(item as Book)
            TypeLibraryObjects.Disk -> setupDiskEditing(item as Disk)
            TypeLibraryObjects.Newspaper -> setupNewspaperEditing(item as Newspaper)
        }
    }

    private fun setupForViewing() {
        binding.saveButton.text = "Назад"
        when (item.objectType) {
            TypeLibraryObjects.Book -> setupBookViewing(item as Book)
            TypeLibraryObjects.Disk -> setupDiskViewing(item as Disk)
            TypeLibraryObjects.Newspaper -> setupNewspaperViewing(item as Newspaper)
        }
    }

    private fun setupBookViewing(item: Book) {
        binding.typeLibraryObject.text = "Книга"
        binding.editName.isEnabled = false
        binding.editName.setText(item.name)
        binding.editId.setText(item.objectId.toString())
        binding.editAuthor.visibility = View.VISIBLE
        binding.editAuthor.isEnabled = false
        binding.editAuthor.setText(item.author)
        binding.author.visibility = View.VISIBLE
        binding.valAccess.text = item.access.toString()
        binding.month.visibility = View.GONE
        binding.monthNewspaper.visibility = View.GONE
        binding.pages.visibility = View.VISIBLE
        binding.editPages.visibility = View.VISIBLE
        binding.editPages.isEnabled = false
        binding.editPages.setText(item.pages.toString())
        binding.releaseNumber.visibility = View.GONE
        binding.editReleaseNumber.visibility = View.GONE
        binding.diskType.visibility = View.GONE
        binding.diskTypeSpinner.visibility = View.GONE
    }

    private fun setupDiskViewing(item: Disk){
        binding.typeLibraryObject.text = "Диск"
        binding.editName.isEnabled = false
        binding.editName.setText(item.name)
        binding.editId.setText(item.objectId.toString())
        binding.editAuthor.visibility = View.GONE
        binding.author.visibility = View.GONE
        binding.valAccess.text = item.access.toString()
        binding.month.visibility = View.GONE
        binding.monthNewspaper.visibility = View.GONE
        binding.pages.visibility = View.GONE
        binding.editPages.visibility = View.GONE
        binding.releaseNumber.visibility = View.GONE
        binding.editReleaseNumber.visibility = View.GONE
        binding.diskType.visibility = View.VISIBLE
        binding.diskTypeSpinner.visibility = View.VISIBLE
        binding.diskTypeSpinner.isEnabled = false
        binding.diskTypeSpinner.setSelection(when (item.type){
            DiskType.DVD -> 1
            DiskType.CD -> 0
        })
    }

    private fun setupNewspaperViewing(item: Newspaper) {
        binding.typeLibraryObject.text = "Газета"
        binding.editName.isEnabled = false
        binding.editName.setText(item.name)
        binding.editId.setText(item.objectId.toString())
        binding.editAuthor.visibility = View.GONE
        binding.author.visibility = View.GONE
        binding.valAccess.text = item.access.toString()
        binding.month.visibility = View.VISIBLE
        binding.monthNewspaper.visibility = View.VISIBLE
        binding.diskTypeSpinner.isEnabled = false
        binding.diskTypeSpinner.setSelection(when (item.month){
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
        })
        binding.pages.visibility = View.GONE
        binding.editPages.visibility = View.GONE
        binding.releaseNumber.visibility = View.VISIBLE
        binding.editReleaseNumber.visibility = View.VISIBLE
        binding.editReleaseNumber.isEnabled = false
        binding.editReleaseNumber.setText(item.releaseNumber.toString())
        binding.diskType.visibility = View.GONE
        binding.diskTypeSpinner.visibility = View.GONE
    }

    private fun setupBookEditing(item: Book) {
        binding.typeLibraryObject.text = "Книга"
        binding.editName.isEnabled = true
        binding.editId.text = item.objectId.toString()
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

    private fun setupDiskEditing(item: Disk) {
        binding.typeLibraryObject.text = "Диск"
        binding.editName.isEnabled = true
        binding.editId.text = item.objectId.toString()
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
            requireContext(),
            R.array.disk_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun setupNewspaperEditing(item: Newspaper) {
        binding.typeLibraryObject.text = "Газета"
        binding.editName.isEnabled = true
        binding.editId.text = item.objectId.toString()
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
            requireContext(),
            R.array.month_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun saveNewItem() {
        if (isNew) {
            when (typeObject) {
                TypeLibraryObjects.Book -> {
                    val newName = binding.editName.text.toString()
                    val newAuthor = binding.editAuthor.text.toString()
                    val newPages = Integer.parseInt(binding.editPages.text.toString())
                    val newId = Integer.parseInt(binding.editId.text.toString())
                    viewModel.addNewItem(
                        listOf(
                            Book(
                            newId,
                            true,
                            newName,
                            TypeLibraryObjects.Book,
                            newPages,
                            newAuthor
                            )
                        )
                    )
                    (activity as MainActivity).closeDetailFragment()
                }
                TypeLibraryObjects.Disk -> {
                    val newName = binding.editName.text.toString()
                    val newId = Integer.parseInt(binding.editId.text.toString())
                    val newTypeOfDisk = when (binding.diskTypeSpinner.selectedItemPosition) {
                        0 -> DiskType.CD
                        1 -> DiskType.DVD
                        else -> DiskType.CD
                    }
                    viewModel.addNewItem(
                        listOf(
                            Disk(
                                objectId = newId,
                                access = true,
                                name = newName,
                                type =  newTypeOfDisk,
                                objectType = TypeLibraryObjects.Disk
                            )
                        )
                    )
                    (activity as MainActivity).closeDetailFragment()
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
                    viewModel.addNewItem(
                        listOf(
                            Newspaper(
                                objectId = newId,
                                access = true,
                                name = newName,
                                releaseNumber = newReleaseNumber,
                                month = newMonth,
                                objectType = TypeLibraryObjects.Newspaper
                            )
                        )
                    )
                    (activity as MainActivity).closeDetailFragment()
                }
            }
        } else {
            (activity as MainActivity).closeDetailFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            (activity as MainActivity).closeDetailFragment()
        }
    }

    companion object {
        const val IS_NEW = "IS_NEW"
        const val BOOK_BUNDLE = "BOOK_BUNDLE"
        const val NEWSPAPER_BUNDLE = "NEWSPAPER_BUNDLE"
        const val DISK_BUNDLE = "DISK_BUNDLE"
        const val TYPE_OBJECT = "TYPE_OBJECT"
        const val CURRENT_ITEM = "CURRENT_ITEM"

        fun newInstance(isNew: Boolean, item: LibraryObjects) = DetailFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_NEW, isNew)
                putString(TYPE_OBJECT, item.objectType.name)
                when (item.objectType) {
                    TypeLibraryObjects.Book -> putParcelable(BOOK_BUNDLE, item as Book)
                    TypeLibraryObjects.Disk -> putParcelable(DISK_BUNDLE, item as Disk)
                    TypeLibraryObjects.Newspaper -> putParcelable(NEWSPAPER_BUNDLE, item as Newspaper)
                }
            }
        }
    }
}