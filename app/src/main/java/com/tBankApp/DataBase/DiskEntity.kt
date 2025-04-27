package com.library.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.library.DiskType
import com.library.TypeLibraryObjects

@Entity(tableName = "disks")
data class DiskEntity(
    @PrimaryKey val objectId: Int,
    val name: String,
    val access: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val type: DiskType,
    val objectType: TypeLibraryObjects = TypeLibraryObjects.Disk
)