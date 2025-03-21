package com.library

class DisksShop: Shops<LibraryObjects>() {
    override fun buy(): Disk {
        val disk = Disk(4567, true, "MIB3", "DVD")
        return disk
    }
}