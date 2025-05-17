package com.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.DependencyContainer
import com.facebook.shimmer.ShimmerFrameLayout
import com.fragments.DetailFragment.Companion.NEW_ITEM
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
import com.library.Month
import com.library.Newspaper
import com.library.R
import com.library.TypeLibraryObjects
import com.interfaces.FragmentCloseListener
import com.viewModels.LibraryViewModel
import com.interfaces.OpenDetailFragment
import com.ScreenState
import com.library.LibraryMode
import com.library.databinding.FragmentListBinding
import com.viewModels.recycler.LibraryAdapter

const val SCROLL_POSITION = "SCROLL_POSITION"

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private val viewModel: LibraryViewModel by viewModels {
        DependencyContainer.getViewModelFactory(requireContext())
    }
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt(SCROLL_POSITION, 0)
        }

        setupRecyclerView()
        setupSearchView()
        setupClickListeners()
        observeViewModel()

        setFragmentResultListener(NEW_ITEM) { _, bundle ->
            bundle.getParcelable<LibraryObjects>(NEW_ITEM)?.let {
                viewModel.addNewItem(it)
            }
            closeListener?.closeDetailFragment()
        }

        binding.recyclerView.post {
            binding.recyclerView.layoutManager?.scrollToPosition(scrollPosition)
        }
    }

    private fun setupRecyclerView() {
        adapter = LibraryAdapter(
            onItemClick = { item ->
                openDetailListener?.showDetail(item, false)
            },
            onItemLongClick = { book ->
                if (viewModel.currentMode.value == LibraryMode.GOOGLE) {
                    viewModel.saveGoogleBook(book)
                    Toast.makeText(requireContext(), "Книга сохранена", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ListFragment.adapter
            addOnScrollListener(createScrollListener())
        }
    }

    private fun createScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val lastVisible = layoutManager.findLastVisibleItemPosition()

            when (val state = viewModel.screenState.value) {
                is ScreenState.Content -> {
                    if (firstVisible < (currentLimit / 3) && state.canLoadPrevious) {
                        viewModel.loadPreviousItems()
                    }
                    if (lastVisible > (currentLimit / 3 * 2) && state.canLoadMore) {
                        viewModel.loadMoreItems()
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupSearchView() {
        binding.etAuthor.doAfterTextChanged { validateSearch() }
        binding.etTitle.doAfterTextChanged { validateSearch() }

        binding.btnSearch.setOnClickListener {
            val author = binding.etAuthor.text.toString()
            val title = binding.etTitle.text.toString()

            if (author.length >= 3 || title.length >= 3) {
                viewModel.searchGoogleBooks(
                    author.takeIf { it.isNotEmpty() },
                    title.takeIf { it.isNotEmpty() }
                )
            }
        }

        binding.btnShowLibrary.setOnClickListener {
            viewModel.switchToLocalMode()
        }

        validateSearch()
    }

    private fun validateSearch() {
        val authorValid = (binding.etAuthor.text?.length ?: 0) >= 3
        val titleValid = (binding.etTitle.text?.length ?: 0) >= 3
        binding.btnSearch.isEnabled = authorValid || titleValid
    }

    private fun setupClickListeners() {
        binding.retryButton.setOnClickListener {
            viewModel.loadInitialData()
        }

        binding.addBook.setOnClickListener {
            if (canAddNewItem()) {
                val newBook = Book(
                    objectId = generateNewId(),
                    access = false,
                    name = "",
                    objectType = TypeLibraryObjects.Book,
                    pages = 0,
                    author = ""
                )
                openDetailListener?.showDetail(newBook, true)
            }
        }

        binding.addDisk.setOnClickListener {
            if (canAddNewItem()) {
                val newDisk = Disk(
                    objectId = generateNewId(),
                    access = false,
                    name = "",
                    type = DiskType.CD,
                    objectType = TypeLibraryObjects.Disk
                )
                openDetailListener?.showDetail(newDisk, true)
            }
        }

        binding.addNewspaper.setOnClickListener {
            if (canAddNewItem()) {
                val newNewspaper = Newspaper(
                    objectId = generateNewId(),
                    access = false,
                    name = "",
                    releaseNumber = 0,
                    month = Month.January,
                    objectType = TypeLibraryObjects.Newspaper
                )
                openDetailListener?.showDetail(newNewspaper, true)
            }
        }

        binding.sortByName.setOnClickListener {
            viewModel.setSortByName(true)
            scrollToTop()
        }

        binding.sortByDate.setOnClickListener {
            viewModel.setSortByName(false)
            scrollToTop()
        }

        binding.btnShowSearch.setOnClickListener {
            viewModel.switchToGoogleMode()
        }
    }

    private fun generateNewId(): Int {
        return (viewModel.items.value?.maxOfOrNull { it.objectId } ?: 0) + 1
    }

    private fun canAddNewItem(): Boolean {
        return when (viewModel.screenState.value) {
            is ScreenState.Content -> true
            is ScreenState.AddingItem -> {
                Toast.makeText(
                    requireContext(),
                    "Дождитесь окончания добавления текущего объекта",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                Toast.makeText(
                    requireContext(),
                    "Невозможно добавить объект в текущем состоянии",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.currentMode.asLiveData().observe(viewLifecycleOwner) { mode ->
            when (mode) {
                LibraryMode.LOCAL -> showLibraryState()
                LibraryMode.GOOGLE -> showSearchState()
            }
        }

        viewModel.screenState.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Loading -> showLoadingState()
                is ScreenState.Content -> showContentState(state)
                is ScreenState.Error -> showErrorState(state)
                is ScreenState.Empty -> showEmptyState()
                is ScreenState.AddingItem -> showAddingState(state)
                is ScreenState.PaginationState -> updatePaginationState(state)
            }
        }

        viewModel.items.asLiveData().observe(viewLifecycleOwner) { items ->
            if (viewModel.currentMode.value == LibraryMode.LOCAL) {
                adapter.submitList(items)
            }
        }

        viewModel.googleBooks.asLiveData().observe(viewLifecycleOwner) { books ->
            if (viewModel.currentMode.value == LibraryMode.GOOGLE) {
                adapter.submitList(books)
            }
        }

        viewModel.sortByName.asLiveData().observe(viewLifecycleOwner) { sortByName ->
            updateSortButtons(sortByName)
        }
    }

    private fun showLibraryState() {
        with(binding) {
            searchPanel.visibility = View.GONE
            sortByName.visibility = View.VISIBLE
            sortByDate.visibility = View.VISIBLE
            buttonContainer.visibility = View.VISIBLE
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun showSearchState() {
        with(binding) {
            shimmerViewContainer.stopShimmer()
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.GONE
            searchPanel.visibility = View.VISIBLE
            sortByName.visibility = View.GONE
            sortByDate.visibility = View.GONE
            buttonContainer.visibility = View.GONE
            errorContainer.visibility = View.GONE
            emptyState.visibility = View.GONE
        }
    }

    private fun showLoadingState() {
        with(binding) {
            shimmerViewContainer.visibility = View.VISIBLE
            shimmerViewContainer.startShimmer()
            recyclerView.visibility = View.GONE
            errorContainer.visibility = View.GONE
            emptyState.visibility = View.GONE
            buttonContainer.visibility = View.GONE
        }
    }

    private fun showContentState(state: ScreenState.Content) {
        with(binding) {
            shimmerViewContainer.stopShimmer()
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            errorContainer.visibility = View.GONE
            emptyState.visibility = View.GONE

            loadMoreProgress.visibility = if (state.canLoadMore) View.VISIBLE else View.GONE
            loadPreviousProgress.visibility = if (state.canLoadPrevious) View.VISIBLE else View.GONE

            when (viewModel.currentMode.value) {
                LibraryMode.LOCAL -> {
                    buttonContainer.visibility = View.VISIBLE
                    sortByName.visibility = View.VISIBLE
                    sortByDate.visibility = View.VISIBLE
                }
                LibraryMode.GOOGLE -> {
                    buttonContainer.visibility = View.GONE
                    sortByName.visibility = View.GONE
                    sortByDate.visibility = View.GONE
                }
                null -> {}
            }
        }
    }

    private fun updatePaginationState(state: ScreenState.PaginationState) {
        with(binding) {
            if (state.isLoadingMore) {
                loadMoreProgress.visibility = View.VISIBLE
            } else {
                loadMoreProgress.visibility = View.GONE
            }

            if (state.isLoadingPrevious) {
                loadPreviousProgress.visibility = View.VISIBLE
            } else {
                loadPreviousProgress.visibility = View.GONE
            }

            state.error?.let {
                Toast.makeText(context, "Ошибка пагинации: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showErrorState(state: ScreenState.Error) {
        with(binding) {
            shimmerViewContainer.stopShimmer()
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.GONE
            emptyState.visibility = View.GONE
            buttonContainer.visibility = View.GONE
            errorContainer.visibility = View.VISIBLE

            errorMessage.text = state.message
        }
    }

    private fun showEmptyState() {
        with(binding) {
            shimmerViewContainer.stopShimmer()
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.GONE
            errorContainer.visibility = View.GONE

            emptyState.visibility = View.VISIBLE
            emptyMessage.text = if (viewModel.currentMode.value == LibraryMode.LOCAL) {
                "Ваша библиотека пуста"
            } else {
                "Книги не найдены"
            }
        }
    }

    private fun showAddingState(state: ScreenState.AddingItem) {
        with(binding) {
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            errorContainer.visibility = View.GONE
            emptyState.visibility = View.GONE

            addProgressBar.visibility = View.VISIBLE
            addProgressBar.progress = (state.progress * 100).toInt()

            buttonContainer.visibility = View.VISIBLE
            addBook.isEnabled = false
            addDisk.isEnabled = false
            addNewspaper.isEnabled = false
        }
    }

    private fun updateSortButtons(sortByName: Boolean) {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.purple_500)
        val inactiveColor = ContextCompat.getColor(requireContext(), R.color.gray_shimmer)

        binding.sortByName.backgroundTintList = ColorStateList.valueOf(
            if (sortByName) activeColor else inactiveColor
        )
        binding.sortByDate.backgroundTintList = ColorStateList.valueOf(
            if (!sortByName) activeColor else inactiveColor
        )
    }

    private fun scrollToTop() {
        binding.recyclerView.layoutManager?.scrollToPosition(0)
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

    companion object {
        private const val currentLimit = 30
    }
}