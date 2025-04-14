package com.library.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.library.LibraryObjects
import com.library.R
import com.library.activity.DetailFragment.Companion.CURRENT_ITEM
import com.library.activity.DetailFragment.Companion.IS_NEW
import com.library.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
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
        }

        setupFragments()
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
            setupFragments()
        }
    }

    private fun setupFragments() {
        if (isLandscape) {
            supportFragmentManager.beginTransaction()
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
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, ListFragment())
                .commit()
        }
//            if (currentDetailItem != null) {
//                supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.nav_host_fragment, DetailFragment.newInstance(isNewItem, currentDetailItem!!))
//                    .addToBackStack(null)
//                    .commit()
//            } else {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.nav_host_fragment, ListFragment())
//                    .commit()
//            }
    }


    fun showDetail(item: LibraryObjects, isNew: Boolean) {
        currentDetailItem = item
        isNewItem = isNew

        val detailFragment = DetailFragment.newInstance(isNew, item)
        if (isLandscape) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detailContainer, detailFragment)
                .commitNow()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, detailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    fun closeDetailFragment() {
        currentDetailItem = null
        if (isLandscape) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.detailContainer, EmptyFragment())
                .commit()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    fun scrollToNewItem() {
        val listFragment = supportFragmentManager.findFragmentById(R.id.listFragment) as ListFragment
        listFragment.scrollToLastItem()
    }

    private fun openFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, ListFragment())
            .commit()
    }
}