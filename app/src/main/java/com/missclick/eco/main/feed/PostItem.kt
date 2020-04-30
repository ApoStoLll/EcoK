package com.missclick.eco.main.feed

import android.os.Parcel
import android.os.Parcelable

data class PostItem(
    val username: String,
    val action: String,
    val score: Int,
    var time: Int = 0,
    var description : String = "NULL",
    var imageName : String = "NULL"):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(action)
        parcel.writeInt(score)
        parcel.writeInt(time)
        parcel.writeString(description)
        parcel.writeString(imageName)
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