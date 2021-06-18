package com.oblessing.mediapark.network

import com.google.gson.annotations.SerializedName

data class ArticleListEntity(
    @SerializedName("totalArticles")
    val totalArticles: Int,
    @SerializedName("articles")
    val articles: List<ArticleEntity>?,
)

data class ArticleEntity(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("source")
    val source: SourceEntity
)

data class SourceEntity(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String)