package com.oblessing.mediapark.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchCacheEntity(
    @PrimaryKey
    @ColumnInfo(name = "keyword")
    val keyword: String
)