package com.oblessing.mediapark.di

import android.content.Context
import androidx.room.Room
import com.oblessing.mediapark.database.SearchDao
import com.oblessing.mediapark.database.SearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): SearchDatabase =
        Room.databaseBuilder(context, SearchDatabase::class.java, SearchDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun providesSearchCacheDao(database: SearchDatabase): SearchDao = database.searchDao()
}