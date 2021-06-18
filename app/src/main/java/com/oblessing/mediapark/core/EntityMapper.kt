package com.oblessing.mediapark.core

interface EntityMapper<EntityModel, DomainModel> {
    fun mapFromEntity(entity: EntityModel): DomainModel
}