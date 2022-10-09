package com.yang.lib_common.data

import android.os.Parcel
import android.os.Parcelable

/**
 * @Author Administrator
 * @ClassName MediaInfoBean
 * @Description
 * @Date 2021/8/6 14:14
 */
class MediaInfoBean() :Parcelable {
    var fileName: String? = null
    var filePath: String? = null
    var fileCreateTime: Long? = null
    var fileDurationTime: String? = null
    var fileSize: String? = null
    /**
     * 0 图片 1 视频
     */
    var fileType:Int = -1
    var isSelect = false

    /**
     * 用于排序的position
     */
    var selectPosition:Int = 0

    constructor(parcel: Parcel) : this() {
        fileName = parcel.readString()
        filePath = parcel.readString()
        fileCreateTime = parcel.readValue(Long::class.java.classLoader) as? Long
        fileDurationTime = parcel.readString()
        fileSize = parcel.readString()
        fileType = parcel.readInt()
        isSelect = parcel.readByte() != 0.toByte()
        selectPosition = parcel.readInt()
    }

    override fun toString(): String {
        return "MediaInfoBean(fileName=$fileName, filePath=$filePath, fileCreateTime=$fileCreateTime, fileDurationTime=$fileDurationTime, fileSize=$fileSize, fileType=$fileType, isSelect=$isSelect)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeString(filePath)
        parcel.writeValue(fileCreateTime)
        parcel.writeString(fileDurationTime)
        parcel.writeString(fileSize)
        parcel.writeInt(fileType)
        parcel.writeByte(if (isSelect) 1 else 0)
        parcel.writeInt(selectPosition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaInfoBean> {
        override fun createFromParcel(parcel: Parcel): MediaInfoBean {
            return MediaInfoBean(parcel)
        }

        override fun newArray(size: Int): Array<MediaInfoBean?> {
            return arrayOfNulls(size)
        }
    }


}