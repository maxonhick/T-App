package com.library

import android.os.Parcel
import android.os.Parcelable

class Newspaper(
    objectId: Int,
    access: Boolean,
    name: String,
    val releaseNumber: Int,
    val month: Month,
    objectType: TypeLibraryObjects
): LibraryObjects(objectId, access, name, objectType), Readable, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readInt(),
        Month.valueOf(parcel.readString() ?: "JANUARY"),
        TypeLibraryObjects.valueOf(parcel.readString() ?: "NEWSPAPER")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(objectId)
        parcel.writeByte(if (access) 1 else 0)
        parcel.writeString(name)
        parcel.writeInt(releaseNumber)
        parcel.writeString(month.name)
        parcel.writeString(objectType.name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Newspaper> {
        override fun createFromParcel(parcel: Parcel): Newspaper = Newspaper(parcel)
        override fun newArray(size: Int): Array<Newspaper?> = arrayOfNulls(size)
    }
    override fun longInformation() {
        val possible = if (access) "Да" else "Нет"
        println("Газета выпуск: $releaseNumber от месяца: $month газеты $name c id: $objectId доступен: $possible")
    }

    override fun returnObject() {
        if (access) {
            println("Газета $objectId находится в библиотеке, её нельзя вернуть")
        } else {
            access = true
            println("Газету $objectId вернули в библиотеку")
        }
    }

    override fun getRead() {
        if (access) {
            access = false
            println("Газету $objectId взяли в читальный зал")
        } else {
            println("Газету уже кто-то взял")
        }
    }
}