package com.mappers

import com.database.entites.DiskEntity
import com.library.Disk
import com.library.TypeLibraryObjects
import javax.inject.Inject

class DiskMapper @Inject constructor() {

    // Domain -> Entity
    fun toEntity(disk: Disk): DiskEntity = DiskEntity(
        objectId = disk.objectId,
        name = disk.name,
        type = disk.type,
        access = disk.access,
        createdAt = disk.createdAt,
        objectType = TypeLibraryObjects.Disk
    )

    // Entity -> Domain
    fun toDomain(entity: DiskEntity): Disk = Disk(
        objectId = entity.objectId,
        name = entity.name,
        type = entity.type,
        access = entity.access,
        objectType = TypeLibraryObjects.Disk,
        createdAt = entity.createdAt
    )
}