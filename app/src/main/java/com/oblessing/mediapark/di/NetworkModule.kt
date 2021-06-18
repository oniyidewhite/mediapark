package com.oblessing.mediapark.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oblessing.mediapark.core.debug
import com.oblessing.mediapark.network.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import com.oblessing.mediapark.BuildConfig
import com.oblessing.mediapark.core.EntityMapper
import com.oblessing.mediapark.network.ArticleListEntity
import com.oblessing.mediapark.network.ArticleMapper
import com.oblessing.mediapark.model.Article
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)

        debug {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            okhttp.addNetworkInterceptor(logging)
        }

        return okhttp.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

    @Singleton
    @Provides
    fun provideMovieWebService(retrofit: Retrofit): WebService =
        retrofit.create(WebService::class.java)

    @Singleton
    @Provides
    @Named("api_key")
    fun provideApiKey() = BuildConfig.PRIVATE_KEY

    @Singleton
    @Provides
    fun provideMovieEntityMapper(mapper: ArticleMapper): EntityMapper<ArticleListEntity?, List<Article>?> =
        mapper
}