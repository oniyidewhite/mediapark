package com.oblessing.mediapark.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.oblessing.mediapark.model.Article
import com.oblessing.mediapark.network.ArticleListEntity
import com.oblessing.mediapark.network.ArticleMapper
import com.oblessing.mediapark.network.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class ArticleRepositoryTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var webService: WebService
    private lateinit var mapper: ArticleMapper
    private lateinit var apikey: String
    private lateinit var repository: ArticleRepository

    @Before
    fun setUp() {
        apikey = "test"
        webService = mock()
        mapper = ArticleMapper()
        repository = ArticleRepository(apikey, mapper, webService)
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun `fetch articles should null if request fails`() = runBlockingTest {
        val articleListEntity: ArticleListEntity? = null

        whenever(webService.articles(apikey)).thenReturn(articleListEntity)

        repository.fetchArticles().collect(collector = object : FlowCollector<List<Article>?>{
            override suspend fun emit(value: List<Article>?) {
                assert(value == null)
            }
        })
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun `fetch articles should data if request successful`() = runBlockingTest {
        val articleListEntity = ArticleListEntity(0, listOf())

        whenever(webService.articles(apikey)).thenReturn(articleListEntity)

        repository.fetchArticles().collect(collector = object : FlowCollector<List<Article>?>{
            override suspend fun emit(value: List<Article>?) {
                assert(value != null)
            }
        })
    }
}

@ExperimentalCoroutinesApi
class CoroutineTestRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}