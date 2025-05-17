package com.activities

import android.app.Application
import com.DependencyContainer

class LibraryApp: Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyContainer.init(this)
    }
}