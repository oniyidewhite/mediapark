package com.oblessing.mediapark.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchCacheEntity::class], version = 1)
abstract class SearchDatabase: RoomDatabase() {
    abstract fun searchDao(): SearchDao

    companion object {
        const val DATABASE_NAME = "db_history"
    }
}