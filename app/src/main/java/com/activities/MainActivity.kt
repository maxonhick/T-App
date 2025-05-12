package com.library.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.fragments.DetailFragment
import com.fragments.DetailFragment.Companion.CURRENT_ITEM
import com.fragments.DetailFragment.Companion.IS_NEW
import com.fragments.EmptyFragment
import com.fragments.ListFragment
import com.interfaces.FragmentCloseListener
import com.interfaces.OpenDetailFragment
import com.library.LibraryObjects
import com.library.R
import com.library.databinding.ActivityMainBinding
import com.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentCloseListener, OpenDetailFragment {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Восстановление состояния при повороте
        if (savedInstanceState != null) {
            viewModel.restoreState(
                item = savedInstanceState.getParcelable(CURRENT_ITEM),
                isNew = savedInstanceState.getBoolean(IS_NEW, false)
            )
        }

        // 2. Настройка фрагментов
        setupFragments()

        // 3. Наблюдение за изменениями
        observeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущее состояние
        viewModel.currentState.value?.let { (item, isNew) ->
            outState.putParcelable(CURRENT_ITEM, item)
            outState.putBoolean(IS_NEW, isNew)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Обновляем конфигурацию
        viewModel.updateOrientation(newConfig.orientation)
        setupFragments()
    }

    // --- Основные методы ---
    private fun setupFragments() {
        if (viewModel.isLandscape) {
            setupLandscapeMode()
        } else {
            setupPortraitMode()
        }
    }

    private fun setupLandscapeMode() {
        binding.apply {
            listFragment.visibility = View.VISIBLE
            detailContainer.visibility = View.VISIBLE
        }

        supportFragmentManager.commit {
            replace(R.id.listFragment, ListFragment())
            viewModel.currentState.value?.let { (item, isNew) ->
                replace(R.id.detailContainer, DetailFragment.newInstance(isNew, item!!))
            } ?: run {
                replace(R.id.detailContainer, EmptyFragment())
            }
        }
    }

    private fun setupPortraitMode() {
        binding.apply {
            listFragment.visibility = View.GONE
            detailContainer.visibility = View.GONE
        }

        supportFragmentManager.commit {
            viewModel.currentState.value?.let { (item, isNew) ->
                replace(R.id.nav_host_fragment, DetailFragment.newInstance(isNew, item!!))
                addToBackStack(null)
            } ?: run {
                replace(R.id.nav_host_fragment, ListFragment())
            }
        }
    }

    private fun observeViewModel() {
        viewModel.orientationChanged.observe(this){
            setupFragments()
        }
    }

    // --- Интерфейсные методы ---
    override fun showDetail(item: LibraryObjects, isNew: Boolean) {
        viewModel.setCurrentItem(item, isNew)
        if (viewModel.isLandscape) {
            supportFragmentManager.commit {
                replace(R.id.detailContainer, DetailFragment.newInstance(isNew, item))
            }
        } else {
            supportFragmentManager.commit {
                replace(R.id.nav_host_fragment, DetailFragment.newInstance(isNew, item))
                addToBackStack(null)
            }
        }
    }

    override fun closeDetailFragment() {
        viewModel.clearCurrentItem()
        if (viewModel.isLandscape) {
            supportFragmentManager.commit {
                replace(R.id.detailContainer, EmptyFragment())
            }
        } else {
            supportFragmentManager.commit {
                replace(R.id.nav_host_fragment, ListFragment())
            }
        }
    }
}