package com.oblessing.mediapark.database

import com.oblessing.mediapark.core.EntityMapper
import javax.inject.Inject

class SearchMapper @Inject constructor() : EntityMapper<SearchCacheEntity, String> {
    override fun mapFromEntity(entity: SearchCacheEntity): String {
        return entity.keyword
    }

    fun mapToEntity(value: String): SearchCacheEntity {
        return SearchCacheEntity(value)
    }
}