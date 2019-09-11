package com.pulmwine.processweb.aidl.error

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by xud on 2017/8/22.
 */

class AidlError() : Parcelable {

    var code: Int = 0
    var message: String? = null
    var extra: String? = null


    constructor(parcel: Parcel) : this() {
        code = parcel.readInt()
        message = parcel.readString()
        extra = parcel.readString()
    }

    constructor(code: Int, message: String) : this() {
        this.code = code
        this.message = message
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(message)
        parcel.writeString(extra)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AidlError> {
        override fun createFromParcel(parcel: Parcel): AidlError {
            return AidlError(parcel)
        }

        override fun newArray(size: Int): Array<AidlError?> {
            return arrayOfNulls(size)
        }
    }


}
