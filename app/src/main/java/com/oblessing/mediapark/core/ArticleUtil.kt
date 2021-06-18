package com.oblessing.mediapark.core

import android.content.Context
import com.oblessing.mediapark.model.Article

object ArticleUtil {
    fun openArticle(context: Context?, article: Article) {
        context?.openUrlInTab(article.contentUrl)
    }
}