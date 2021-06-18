package com.oblessing.mediapark.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SearchCacheEntity)

    @Query("SELECT * FROM search_history")
    fun all(): Flow<List<SearchCacheEntity>>

    fun getHistoryDistinctUntilChanged() =
        all().distinctUntilChanged()
}