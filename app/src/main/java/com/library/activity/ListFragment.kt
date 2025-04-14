package com.library.activity

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
import com.library.Month
import com.library.Newspaper
import com.library.R
import com.library.TypeLibraryObjects
import com.library.databinding.ActivityMainBinding
import com.library.databinding.FragmentListBinding
import com.tBankApp.recycler.LibraryAdapter
import kotlinx.coroutines.launch

const val SCROLL_POSITION = "SCROLL_POSITION"

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private val viewModel: LibraryViewModel by activityViewModels()
    private lateinit var adapter: LibraryAdapter
    private var scrollPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListBinding.inflate(layoutInflater)

        setupRecyclerView()
        setupClickListeners()
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
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (parentFragmentManager.findFragmentById(R.id.nav_host_fragment) !is DetailFragment) {
                    navigateToDetail(item, false)
                }
            } else {
                navigateToDetail(item, false)
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this.adapter
        }
    }

    private fun setupClickListeners() {
        binding.addBook.setOnClickListener {
            val newBook = Book(viewModel.getSize() + 1, false, "", TypeLibraryObjects.Book, 0, "")
            navigateToDetail(
                newBook,
                true
            )
        }

        binding.addDisk.setOnClickListener {
            val newDisk = Disk(viewModel.getSize() + 1, false, "", DiskType.CD, TypeLibraryObjects.Disk)
            navigateToDetail(
                newDisk,
                true
            )

        }

        binding.addNewspaper.setOnClickListener {
            val newNewspaper = Newspaper(viewModel.getSize() + 1, false, "", 0, Month.July, TypeLibraryObjects.Newspaper)
            navigateToDetail(
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

    fun scrollToLastItem() {
        binding.recyclerView.post {
            val lastPosition = adapter.itemCount - 1
            if (lastPosition >= 0) {
                binding.recyclerView.scrollToPosition(lastPosition)
            }
        }
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

    private fun navigateToDetail(item: LibraryObjects, isNew: Boolean) {
        (activity as MainActivity).showDetail(item, isNew)
    }
}