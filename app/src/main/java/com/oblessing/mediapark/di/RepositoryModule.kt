package com.oblessing.mediapark.di

import com.oblessing.mediapark.core.EntityMapper
import com.oblessing.mediapark.database.SearchDao
import com.oblessing.mediapark.database.SearchMapper
import com.oblessing.mediapark.network.ArticleListEntity
import com.oblessing.mediapark.network.WebService
import com.oblessing.mediapark.model.Article
import com.oblessing.mediapark.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        @Named("api_key") apiKey: String,
        networkMapper: EntityMapper<ArticleListEntity?, List<Article>?>,
        searchMapper: SearchMapper,
        searchDao: SearchDao,
        webService: WebService
    ) = ArticleRepository(apiKey, networkMapper, webService, searchMapper, searchDao)
}