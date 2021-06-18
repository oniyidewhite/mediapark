package com.oblessing.mediapark.di

import com.oblessing.mediapark.core.support.AssistedViewModelFactory
import com.oblessing.mediapark.core.support.MavericksViewModelComponent
import com.oblessing.mediapark.core.support.ViewModelKey
import com.oblessing.mediapark.screens.filterDate.FilterViewModel
import com.oblessing.mediapark.screens.news.NewsListingViewModel
import com.oblessing.mediapark.screens.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(NewsListingViewModel::class)
    fun provideNewsListingViewModelFactory(factory: NewsListingViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun provideSearchViewModelFactory(factory: SearchViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    fun provideFilterViewModelFactory(factory: FilterViewModel.Factory): AssistedViewModelFactory<*, *>
}