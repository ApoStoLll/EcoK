package com.missclick.eco.main.profile

import android.os.Parcel
import android.os.Parcelable

data class PositiveItem(
    val id: Int,
    val action: String,
    val score: Int,
    var time: String = "",
    var description : String = "",
    var share: Boolean = false,
    var imageName : String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(action)
        parcel.writeInt(score)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PositiveItem> {
        override fun createFromParcel(parcel: Parcel): PositiveItem {
            return PositiveItem(parcel)
        }

        override fun newArray(size: Int): Array<PositiveItem?> {
            return arrayOfNulls(size)
        }
    }
}