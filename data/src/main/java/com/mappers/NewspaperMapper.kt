package com.mappers

import com.database.entites.NewspaperEntity
import com.library.Newspaper
import com.library.TypeLibraryObjects

class NewspaperMapper {

    // Domain -> Entity
    fun toEntity(newspaper: Newspaper): NewspaperEntity = NewspaperEntity(
        objectId = newspaper.objectId,
        name = newspaper.name,
        releaseNumber = newspaper.releaseNumber,
        month = newspaper.month,
        access = newspaper.access,
        createdAt = newspaper.createdAt,
        objectType = TypeLibraryObjects.Newspaper
    )

    // Entity -> Domain
    fun toDomain(entity: NewspaperEntity): Newspaper = Newspaper(
        objectId = entity.objectId,
        name = entity.name,
        releaseNumber = entity.releaseNumber,
        month = entity.month,
        access = entity.access,
        createdAt = entity.createdAt,
        objectType = TypeLibraryObjects.Newspaper
    )
}