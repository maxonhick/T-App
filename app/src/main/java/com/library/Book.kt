package com.library

import android.os.Parcel
import android.os.Parcelable

class Book(
    objectId: Int,
    access: Boolean,
    name: String,
    objectType: TypeLibraryObjects,
    val pages: Int,
    val author: String
): LibraryObjects(objectId, access, name, objectType), Homeable, Readable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        TypeLibraryObjects.valueOf(parcel.readString() ?: "BOOK"),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun longInformation() {
        val possible = if (access) "Да" else "Нет"
        println("книга: $name ($pages стр.) автора: $author с id: $objectId доступна: $possible")
    }

    override fun getHome() {
        if (access) {
            access = false
            println("Книга $objectId взяли домой")
        } else {
            println("Книгу уже кто-то взял")
        }
    }

    override fun getRead() {
        if (access) {
            access = false
            println("Книга $objectId взяли в читальный зал")
        } else {
            println("Книгу уже кто-то взял")
        }
    }

    override fun returnObject() {
        if (access) {
            println("Книга $objectId находится в библиотеке, её нельзя вернуть")
        } else {
            access = true
            println("Книгу $objectId вернули в библиотеку")
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(objectId)
        parcel.writeByte(if (access) 1 else 0)
        parcel.writeString(name)
        parcel.writeString(objectType.name)
        parcel.writeInt(pages)
        parcel.writeString(author)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}