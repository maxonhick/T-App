package com.library.activity

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.library.Book
import com.library.Disk
import com.library.DiskType
import com.library.LibraryObjects
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
            val newItem = bundle.getParcelable<LibraryObjects>(requestKey)
            newItem?.let {
                viewModel.addNewItem(it)
            }
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
                is ScreenState.Loading -> showLoadingState()
                is ScreenState.Content -> showContentState(state)
                is ScreenState.Error -> showErrorState(state)
                is ScreenState.Empty -> showEmptyState()
                is ScreenState.AddingItem -> showAddingState(state)
                is ScreenState.PaginationState -> updatePaginationState(state)
            }
        }

        viewModel.paginationState.observe(viewLifecycleOwner) { state ->
            binding.loadMoreProgress.visibility =
                if (state.isLoadingMore) View.VISIBLE else View.GONE
            binding.loadPreviousProgress.visibility =
                if (state.isLoadingPrevious) View.VISIBLE else View.GONE
        }

        viewModel.sortByName.observe(viewLifecycleOwner) { sortByName ->
            updateSortButtons(sortByName)
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
            addOnScrollListener(createScrollListener())
        }
    }

    private fun createScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            val totalItems = layoutManager.itemCount

            when (val state = viewModel.screenState.value) {
                is ScreenState.Content -> {
                    if (firstVisible <= 10 && state.canLoadPrevious) {
                        viewModel.loadPreviousItems()
                    }
                    if (lastVisible >= totalItems - 10 && state.canLoadMore) {
                        viewModel.loadMoreItems()
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupClickListeners() {
        binding.retryButton.setOnClickListener {
            viewModel.retryLoading()
        }

        binding.addBook.setOnClickListener {
            if (canAddNewItem()) {
                val nextId = getNextAvailableId()
                val newBook = Book(
                    objectId = nextId,
                    access = false,
                    name = "",
                    objectType = TypeLibraryObjects.Book,
                    pages = 0,
                    author = ""
                )
                (activity as? OpenDetailFragment)?.showDetail(newBook, true)
            }
        }

        binding.addDisk.setOnClickListener {
            if (canAddNewItem()) {
                val nextId = getNextAvailableId()
                val newDisk = Disk(
                    objectId = nextId,
                    access = false,
                    name = "",
                    type = DiskType.CD,
                    objectType = TypeLibraryObjects.Disk
                )
                (activity as? OpenDetailFragment)?.showDetail(newDisk, true)
            }
        }

        binding.addNewspaper.setOnClickListener {
            if (canAddNewItem()) {
                val nextId = getNextAvailableId()
                val newNewspaper = Newspaper(
                    objectId = nextId,
                    access = false,
                    name = "",
                    releaseNumber = 0,
                    month = Month.January,
                    objectType = TypeLibraryObjects.Newspaper
                )
                (activity as? OpenDetailFragment)?.showDetail(newNewspaper, true)
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
    }

    private fun canAddNewItem(): Boolean {
        return when (val state = viewModel.screenState.value) {
            is ScreenState.Content -> {
                if (state.items.any { it.access } or state.items.isEmpty()) {
                    true
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Достигнут лимит доступных объектов",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            }
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

    private fun getNextAvailableId(): Int {
        return when (val state = viewModel.screenState.value) {
            is ScreenState.Content -> (state.items.maxOfOrNull { it.objectId } ?: 0) + 1
            else -> 1
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            scrollPosition = it.getInt(SCROLL_POSITION)
            binding.recyclerView.post{
                binding.recyclerView.scrollToPosition(scrollPosition)
            }
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
            loadMoreProgress.visibility = View.GONE
            loadPreviousProgress.visibility = View.GONE
        }
    }

    private fun showContentState(state: ScreenState.Content) {
        with(binding) {
            shimmerViewContainer.stopShimmer()
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            errorContainer.visibility = View.GONE
            emptyState.visibility = View.GONE
            buttonContainer.visibility = View.VISIBLE
            addProgressBar.visibility = View.GONE

            adapter.submitList(state.items)

            // Настройка видимости кнопок
            loadMoreProgress.visibility = if (state.canLoadMore) View.VISIBLE else View.GONE
            loadPreviousProgress.visibility = if (state.canLoadPrevious) View.VISIBLE else View.GONE

            addBook.isEnabled = true
            addDisk.isEnabled = true
            addNewspaper.isEnabled = true
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

            retryButton.setOnClickListener {
                state.retryAction?.invoke()
            }
        }
    }

    private fun showEmptyState() {
        with(binding) {
            shimmerViewContainer.stopShimmer()
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.GONE
            errorContainer.visibility = View.GONE
            buttonContainer.visibility = View.VISIBLE
            emptyState.visibility = View.VISIBLE

            emptyMessage.text = ScreenState.Empty.emptyMessage

            // Включаем кнопки добавления, так как список пуст
            addBook.isEnabled = true
            addDisk.isEnabled = true
            addNewspaper.isEnabled = true
        }
    }

    private fun showAddingState(state: ScreenState.AddingItem) {
        with(binding) {
            shimmerViewContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            errorContainer.visibility = View.GONE
            emptyState.visibility = View.GONE

            // Отображаем текущий прогресс добавления
            addProgressBar.visibility = View.VISIBLE
            addProgressBar.progress = (state.progress * 100).toInt()

            // Блокируем кнопки во время добавления
            buttonContainer.visibility = View.VISIBLE
            addBook.isEnabled = false
            addDisk.isEnabled = false
            addNewspaper.isEnabled = false

            adapter.submitList(state.currentItems)
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

            // Показываем ошибку пагинации, если есть
            state.error?.let {
                Toast.makeText(context, "Ошибка загрузки: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

}