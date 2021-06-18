package com.oblessing.mediapark.network

import org.junit.Test

class ArticleMapperTest {
    @Test
    fun `On null the mapper should also return null`() {
        val mapper = ArticleMapper()
        var result = mapper.mapFromEntity(null)
        assert(result == null)

        result = mapper.mapFromEntity(ArticleListEntity(0, null))
        assert(result == null)
    }

    @Test
    fun `On empty the mapper should also be empty`() {
        val mapper = ArticleMapper()
        val result = mapper.mapFromEntity(ArticleListEntity(0, listOf()))
        assert(result != null)
    }

    @Test
    fun `On successful the mapper should return list of articles`() {
        val mapper = ArticleMapper()
        val result = mapper.mapFromEntity(
            ArticleListEntity(
                0,
                listOf(ArticleEntity("a", "b", "c", "d", "e", "f", SourceEntity("g", "h")))
            )
        )
        assert(result?.count() == 1)

        result!![0].let {
            assert(it.title == "a")
            assert(it.subTitle == "c")
            assert(it.contentUrl == "d")
            assert(it.imageUrl == "e")
        }
    }
}