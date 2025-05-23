package com.activities

import android.app.Application
import com.di.ListFragmentComponent
import com.di.ListFragmentComponentProvider
import com.di.MainActivityComponent
import com.di.MainActivityComponentProvider
import com.di.MainComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LibraryApp: Application(), MainActivityComponentProvider, ListFragmentComponentProvider {

    private lateinit var mainComponent: MainComponent

    override fun onCreate() {
        super.onCreate()
        mainComponent = DaggerMainComponent
            .builder()
            .appModule(AppModule())
            .dataModule(DataModule())
            .build()
    }

    override fun getMainActivityComponent(): MainActivityComponent {
        return mainComponent.getMainActivityComponent().create()
    }

    override fun getListFragmentComponent(): ListFragmentComponent {
        return mainComponent.getListFragmentComponent().create()
    }
}