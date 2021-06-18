package com.oblessing.mediapark.network

import com.oblessing.mediapark.core.EntityMapper
import com.oblessing.mediapark.model.Article
import javax.inject.Inject

class ArticleMapper @Inject constructor(): EntityMapper<ArticleListEntity?, List<Article>?> {
    override fun mapFromEntity(entity: ArticleListEntity?): List<Article>? {
        return entity?.articles?.map {
            Article(
                imageUrl = it.image,
                subTitle = it.content,
                title = it.title,
                contentUrl = it.url
            )
        }
    }
}