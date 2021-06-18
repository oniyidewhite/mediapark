package com.oblessing.mediapark.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {
    @GET("top-headlines")
    suspend fun articles(@Query("token") key: String): ArticleListEntity?

    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("sortby") sortby: String,
        @Query("in") searchIn: String,
        @Query("token") key: String
    ): ArticleListEntity?
}