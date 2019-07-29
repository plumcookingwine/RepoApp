package com.plumcookingwine.webview.callback

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by xud on 2017/8/22.
 */

class AidlError : Parcelable {

    var code: Int = 0
    var message: String? = null
    var extra: String? = null

    constructor(code: Int, message: String) {
        this.code = code
        this.message = message
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.code)
        dest.writeString(this.message)
        dest.writeString(this.extra)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.code = `in`.readInt()
        this.message = `in`.readString()
        this.extra = `in`.readString()
    }

    companion object {

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<AidlError> = object : Parcelable.Creator<AidlError> {
            override fun createFromParcel(source: Parcel): AidlError {
                return AidlError(source)
            }

            override fun newArray(size: Int): Array<AidlError?> {
                return arrayOfNulls(size)
            }
        }
    }
}
