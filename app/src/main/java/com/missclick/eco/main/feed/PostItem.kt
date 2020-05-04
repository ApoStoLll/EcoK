package com.missclick.eco.main.feed

import android.os.Parcel
import android.os.Parcelable

data class PostItem(
    val username: String,
    val action: String,
    val score: Int,
    val description: String = "NULL",
    val time: Int = 0,
    var imageName: String = "NULL",
    var imageProfileName: String,
    val share: Boolean = true
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(action)
        parcel.writeInt(score)
        parcel.writeString(description)
        parcel.writeInt(time)
        parcel.writeString(imageName)
        parcel.writeString(imageProfileName)
        parcel.writeByte(if (share) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostItem> {
        override fun createFromParcel(parcel: Parcel): PostItem {
            return PostItem(parcel)
        }

        override fun newArray(size: Int): Array<PostItem?> {
            return arrayOfNulls(size)
        }
    }
}