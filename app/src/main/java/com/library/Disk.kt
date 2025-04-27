package com.library

import android.os.Parcel
import android.os.Parcelable

class Disk(
    objectId: Int,
    access: Boolean,
    name: String,
    val type: DiskType,
    objectType: TypeLibraryObjects,
    createdAt: Long = System.currentTimeMillis()
): LibraryObjects(objectId, access, name, objectType, createdAt), Homeable, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        DiskType.valueOf(parcel.readString() ?: "CD"),
        TypeLibraryObjects.valueOf(parcel.readString() ?: "DISK"),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(objectId)
        parcel.writeByte(if (access) 1 else 0)
        parcel.writeString(name)
        parcel.writeString(type.name)
        parcel.writeString(objectType.name)
        parcel.writeLong(createdAt)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Disk> {
        override fun createFromParcel(parcel: Parcel): Disk = Disk(parcel)
        override fun newArray(size: Int): Array<Disk?> = arrayOfNulls(size)
    }

    override fun longInformation() {
        val possible = if (access) "Да" else "Нет"
        println("$type $name доступен: $possible")
    }

    override fun returnObject() {
        if (access) {
            println("Диск $objectId находится в библиотеке, его нельзя вернуть")
        } else {
            access = true
            println("Диск $objectId вернули в библиотеку")
        }
    }

    override fun getHome() {
        if (access) {
            access = false
            println("Диск $objectId взяли домой")
        } else {
            println("Диск уже кто-то взял")
        }
    }
}