package com.oblessing.mediapark.repository

import com.oblessing.mediapark.core.EntityMapper
import com.oblessing.mediapark.database.SearchDao
import com.oblessing.mediapark.database.SearchMapper
import com.oblessing.mediapark.network.ArticleListEntity
import com.oblessing.mediapark.network.WebService
import com.oblessing.mediapark.model.Article
import com.oblessing.mediapark.model.SearchCriteria
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class ArticleRepository(
    private val apiKey: String,
    private val networkMapper: EntityMapper<ArticleListEntity?, List<Article>?>,
    private val webService: WebService,
    private val searchMapper: SearchMapper,
    private val store: SearchDao,
) {
    fun fetchArticles(): Flow<List<Article>?> = flow {
        emit(networkMapper.mapFromEntity(webService.articles(key = apiKey)))
    }

    fun findArticles(s: SearchCriteria): Flow<List<Article>?> = flow {
        emit(
            networkMapper.mapFromEntity(
                webService.search(
                    query = s.value.trim(),
                    sortby = s.sortedBy.trim(),
                    from = s.from.trim(),
                    to = s.to.trim(),
                    searchIn = s.searchIn.trim(),
                    key = apiKey.trim()
                )
            )
        )
    }

    fun savedKeywords(): Flow<List<String>> = flow {
        store.getHistoryDistinctUntilChanged().collect {
            emit(it.map { searchMapper.mapFromEntity(it) })
        }
    }

    suspend fun saveKeyword(s: SearchCriteria) {
        store.insert(searchMapper.mapToEntity(s.value.trim()))
    }
}
