package com.library.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
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
    private var closeListener: FragmentCloseListener? = null
    private var openDetailListener: OpenDetailFragment? = null
    private lateinit var shimmerView: ShimmerFrameLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        closeListener = context as? FragmentCloseListener
        openDetailListener = context as? OpenDetailFragment
    }

    override fun onDetach() {
        super.onDetach()
        closeListener = null
        openDetailListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListBinding.inflate(layoutInflater)
        shimmerView = binding.shimmerViewContainer

        setFragmentResultListener(NEW_ITEM) { requestKey, bundle ->
            viewModel.addNewItem(bundle.getParcelable(requestKey)!!)
            closeListener?.closeDetailFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        binding.recyclerView.layoutManager?.scrollToPosition(scrollPosition)

        setupRecyclerView()
        setupClickListeners()
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Loading -> with(binding) {
                    shimmerViewContainer.visibility = View.VISIBLE
                    shimmerViewContainer.startShimmer()
                    recyclerView.visibility = View.GONE
                    errorContainer.visibility = View.GONE
                    buttonContainer.visibility = View.GONE
                    addProgressBar.visibility = View.GONE
                }
                is ScreenState.Content -> with(binding) {
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    errorContainer.visibility = View.GONE
                    buttonContainer.visibility = View.VISIBLE
                    addProgressBar.visibility = View.GONE

                    adapter.submitList(state.items)
                }
                is ScreenState.Error -> with(binding) {
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    errorContainer.visibility = View.VISIBLE
                    buttonContainer.visibility = View.GONE
                    addProgressBar.visibility = View.GONE

                    errorMessage.text = state.message
                }
                is ScreenState.AddingItem -> with(binding) {
                    adapter.submitList(state.currentItems)

                    addProgressBar.visibility = View.VISIBLE
                    buttonContainer.visibility = View.GONE

                    shimmerViewContainer.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    errorContainer.visibility = View.GONE
                }
            }
        }

        binding.recyclerView.adapter = adapter
    }

    private fun setupRecyclerView() {
        adapter = LibraryAdapter { item ->
            openDetailListener?.showDetail(item, false)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this.adapter
        }
    }

    private fun setupClickListeners() {
        binding.retryButton.setOnClickListener {
            viewModel.retryLoading()
        }

        binding.addBook.setOnClickListener {
            if (viewModel.screenState.value !is ScreenState.AddingItem) {
                val nextId = (viewModel.screenState.value as? ScreenState.Content)?.items?.size?.plus(1) ?: 1
                val newBook = Book(nextId, false, "", TypeLibraryObjects.Book, 0, "")
                openDetailListener?.showDetail(
                    newBook,
                    true
                )
            }
        }

        binding.addDisk.setOnClickListener {
            if (viewModel.screenState.value !is ScreenState.AddingItem) {
                val nextId =
                    (viewModel.screenState.value as? ScreenState.Content)?.items?.size?.plus(1) ?: 1
                val newDisk = Disk(nextId, false, "", DiskType.CD, TypeLibraryObjects.Disk)
                openDetailListener?.showDetail(
                    newDisk,
                    true
                )
            }
        }

        binding.addNewspaper.setOnClickListener {
            if (viewModel.screenState.value !is ScreenState.AddingItem) {
                val nextId =
                    (viewModel.screenState.value as? ScreenState.Content)?.items?.size?.plus(1) ?: 1
                val newNewspaper =
                    Newspaper(nextId, false, "", 0, Month.July, TypeLibraryObjects.Newspaper)
                openDetailListener?.showDetail(
                    newNewspaper,
                    true
                )
            }
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