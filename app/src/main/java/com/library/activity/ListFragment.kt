package com.library.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.Month
import com.library.Newspaper
import com.library.R
import com.library.TypeLibraryObjects
import com.library.activity.DetailFragment.Companion.NEW_ITEM
import com.library.databinding.FragmentListBinding
import com.tBankApp.recycler.LibraryAdapter

const val SCROLL_POSITION = "SCROLL_POSITION"

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private val viewModel: LibraryViewModel by activityViewModels()
    private lateinit var adapter: LibraryAdapter
    private var scrollPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListBinding.inflate(layoutInflater)

        setFragmentResultListener(NEW_ITEM) { requestKey, bundle ->
            viewModel.addNewItem(bundle.getParcelable(requestKey)!!)
            (activity as MainActivity).closeDetailFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        binding.recyclerView.layoutManager?.scrollToPosition(scrollPosition)

        setupRecyclerView()
        setupClickListeners()
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        binding.recyclerView.adapter = adapter
    }

    private fun setupRecyclerView() {
        adapter = LibraryAdapter { item ->
            (activity as MainActivity).showDetail(item, false)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this.adapter
        }
    }

    private fun setupClickListeners() {
        binding.addBook.setOnClickListener {
            val newBook = Book(viewModel.getSize() + 1, false, "", TypeLibraryObjects.Book, 0, "")
            (activity as MainActivity).showDetail(
                newBook,
                true
            )
        }

        binding.addDisk.setOnClickListener {
            val newDisk = Disk(viewModel.getSize() + 1, false, "", DiskType.CD, TypeLibraryObjects.Disk)
            (activity as MainActivity).showDetail(
                newDisk,
                true
            )

        }

        binding.addNewspaper.setOnClickListener {
            val newNewspaper = Newspaper(viewModel.getSize() + 1, false, "", 0, Month.July, TypeLibraryObjects.Newspaper)
            (activity as MainActivity).showDetail(
                newNewspaper,
                true
            )
        }
    }

    override fun onPause() {
        super.onPause()
        scrollPosition = (binding.recyclerView.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION, scrollPosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            scrollPosition = it.getInt(SCROLL_POSITION)
            binding.recyclerView.post{
                binding.recyclerView.scrollToPosition(scrollPosition)
            }
        }
    }
}