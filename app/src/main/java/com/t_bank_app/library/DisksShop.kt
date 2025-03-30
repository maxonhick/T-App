package com.t_bank_app.library

class DisksShop: Shops<LibraryObjects>() {
    override fun sell(): Disk {
        val disk = Disk(4567, true, "MIB3", DiskType.DVD)
        return disk
    }
}