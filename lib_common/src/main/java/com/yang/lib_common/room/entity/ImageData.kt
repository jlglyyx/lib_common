package com.yang.lib_common.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yang.lib_common.constant.AppConstant

/**
 *des:
 *
 *@author My Live
 *@date 2021/6/11
 */
data class ImageData(
    val list: MutableList<ImageDataItem>,
    val pageNum: Int?,
    val pageSize: Int?,
    val size: Int?,
    val total: String?
)

@Entity(tableName = "image_data")
data class ImageDataItem(
    @PrimaryKey
    var id: String,
    @ColumnInfo
    var imageName: String?,
    @ColumnInfo
    var imageType: String?,
    @ColumnInfo
    var imageUrl: String?,
    @ColumnInfo
    var imageDesc: String?,
    @ColumnInfo
    var imageTitle: String?,
    @ColumnInfo
    var imageExtraInfo: String?,
    @ColumnInfo
    var createTime: String?,
    @ColumnInfo
    var updateTime: String?
):MultiItemEntity {

    @Ignore
    var mItemType:Int = AppConstant.Constant.ITEM_CONTENT
    @Ignore
    var mTTNativeExpressAd: TTNativeExpressAd? = null

    override fun getItemType(): Int {
        return mItemType
    }
}