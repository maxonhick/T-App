package com.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(subcomponents = [MainActivityComponent::class, ListFragmentComponent::class])
@InstallIn(SingletonComponent::class)
interface MainSubcomponentModule