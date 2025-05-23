package com.di

import dagger.Component
import jakarta.inject.Singleton

@Singleton
@Component(modules = [MainSubcomponentModule::class, AppModule::class, DataModule::class])
interface MainComponent {

    fun getMainActivityComponent(): MainActivityComponent.Factory
    fun getListFragmentComponent(): ListFragmentComponent.Factory
}