package com.library.data

import androidx.room.TypeConverter
import com.library.DiskType
import com.library.Month
import com.library.TypeLibraryObjects

class Converters {
    @TypeConverter
    fun fromDiskType(value: DiskType): String = value.name

    @TypeConverter
    fun toDiskType(value: String): DiskType = enumValueOf(value)

    @TypeConverter
    fun fromMonth(value: Month): String = value.name

    @TypeConverter
    fun toMonth(value: String): Month = enumValueOf(value)

    @TypeConverter
    fun fromTypeLibraryObjects(value: TypeLibraryObjects): String = value.name

    @TypeConverter
    fun toTypeLibraryObjects(value: String): TypeLibraryObjects = enumValueOf(value)
}