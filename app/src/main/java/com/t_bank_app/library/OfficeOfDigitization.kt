package com.t_bank_app.library

class OfficeOfDigitization<in T: LibraryObjects> {
    fun transform(libraryObject: T): Disk? {
        if (((libraryObject is Book) or (libraryObject is Disk)) and libraryObject.access) {
            val disk = Disk(
                objectId = libraryObject.objectId,
                access = true,
                name = libraryObject.name,
                type = DiskType.CD
            )
            return disk
        } else {
            return null
        }
    }
}