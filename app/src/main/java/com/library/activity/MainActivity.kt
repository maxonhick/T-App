package com.library.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.library.LibraryObjects
import com.library.R
import com.library.activity.DetailFragment.Companion.CURRENT_ITEM
import com.library.activity.DetailFragment.Companion.IS_NEW
import com.library.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), FragmentCloseListener {
    private lateinit var binding: ActivityMainBinding
    private var isLandscape: Boolean = false
    private var currentDetailItem: LibraryObjects? = null
    private var isNewItem: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (savedInstanceState != null) {
            currentDetailItem = savedInstanceState.getParcelable(CURRENT_ITEM)
            isNewItem = savedInstanceState.getBoolean(IS_NEW, false)
            setupFragments()
        } else {
            if (isLandscape) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.listFragment, ListFragment())
                    .commit()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.detailContainer, EmptyFragment())
                    .commit()
            } else {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.nav_host_fragment, ListFragment())
                    .commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CURRENT_ITEM, currentDetailItem)
        outState.putBoolean(IS_NEW, isNewItem)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newOrientation = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape != newOrientation) {
            isLandscape = newOrientation
        }
        setupFragments()
    }

    private fun setupFragments() {
        if (isLandscape) {
            // Make sure containers are visible
            binding.listFragment.visibility = View.VISIBLE
            binding.detailContainer.visibility = View.VISIBLE

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.listFragment, ListFragment())
                .commit()

            currentDetailItem?.let {
                showDetail(it, isNewItem)
            } ?: run {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.detailContainer, EmptyFragment())
                    .commit()
            }
        } else {
            binding.listFragment.visibility = View.GONE
            binding.detailContainer.visibility = View.GONE

            currentDetailItem?.let {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.nav_host_fragment, DetailFragment.newInstance(isNewItem, it))
                    .addToBackStack(null)
                    .commit()
            } ?: run {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, ListFragment())
                    .commit()
            }
        }
    }


    fun showDetail(item: LibraryObjects, isNew: Boolean) {
        currentDetailItem = item
        isNewItem = isNew

        val detailFragment = DetailFragment.newInstance(isNew, item)
        if (isLandscape) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.detailContainer, detailFragment)
                .commitNow()
        } else {
            if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) !is DetailFragment) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun closeDetailFragment() {
        currentDetailItem = null
        if (isLandscape) {
            if (!isFinishing && !supportFragmentManager.isStateSaved) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.detailContainer, EmptyFragment())
                    .commit()
            }
        } else {
            if (!isFinishing && !supportFragmentManager.isStateSaved) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, ListFragment())
                    .commit()
            }
        }
    }
}