package com.library

class DisksShop: Shops<LibraryObjects>() {
    override fun sell(): Disk {
        val disk = Disk(4567, true, "MIB3", DiskType.DVD, TypeLibraryObjects.Disk)
        return disk
    }
}