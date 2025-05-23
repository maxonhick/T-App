package com.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viewModels.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
internal object MainActivityModule {

    @Provides
    fun provideMainActivityModel(
        impl: MainViewModel
    ): ViewModel = impl

    @Provides
    fun provideViewModelFactory(
        viewModelProvider: Provider<ViewModel>
    ): ViewModelProvider.Factory {
        return object: ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelProvider.get() as T
            }
        }
    }
}