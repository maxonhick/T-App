package com.library

class OfficeOfDigitization<in T: LibraryObjects> {
    fun transform(libraryObject: T): Disk? {
        if (libraryObject.access) {
            val disk = Disk(
                objectId = libraryObject.objectId,
                access = true,
                name = libraryObject.name,
                type = "CD"
            )
            return disk
        } else {
            return null
        }
    }
}