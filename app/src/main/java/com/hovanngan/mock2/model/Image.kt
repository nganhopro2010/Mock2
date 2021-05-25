package com.hovanngan.mock2.model

import android.os.Parcel
import android.os.Parcelable

class Image : Parcelable {
    var uri: String? = null

    constructor(uri: String) {
        this.uri = uri
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, arg1: Int) {
        dest.writeString(uri)
    }

    constructor(`in`: Parcel) {
        uri= `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Image> = object : Parcelable.Creator<Image> {
            override fun createFromParcel(`in`: Parcel): Image {
                return Image(`in`)
            }

            override fun newArray(size: Int): Array<Image?> {
                return arrayOfNulls(size)
            }
        }
    }
}

