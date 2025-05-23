package com.di

import com.fragments.ListFragment
import com.library.activity.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [ListFragmentModule::class])
interface ListFragmentComponent {

    fun inject(fragment: ListFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): ListFragmentComponent
    }
}