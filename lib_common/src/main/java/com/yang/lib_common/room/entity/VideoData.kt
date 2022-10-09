package com.yang.lib_common.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yang.lib_common.constant.AppConstant

data class VideoData(
    var list: MutableList<VideoDataItem>,
    var pageNum: Int?,
    var pageSize: Int?,
    var size: Int?,
    var total: String?

)
@Entity(tableName = "video_data")
class VideoDataItem :MultiItemEntity{
    @Ignore
    var mItemType:Int = AppConstant.Constant.ITEM_CONTENT
    @Ignore
    var select = false
    @Ignore
    var position = 0
    @PrimaryKey
    var id: String = ""
    @ColumnInfo
    var videoName: String? = null
    @ColumnInfo
    var videoType: String? = null
    @ColumnInfo
    var videoUrl: String? = null
    @ColumnInfo
    var videoTitle: String? = null
    @ColumnInfo
    var createTime: String? = null
    @ColumnInfo
    var updateTime: String? = null
    @ColumnInfo
    var isLarge: Boolean = false
    @ColumnInfo
    var itemId: String? = null
    @ColumnInfo
    var title: String? = null
    @Ignore
    var smartVideoUrls: MutableList<VideoDataItem>? = null

    @Ignore
    var mTTNativeExpressAd: TTNativeExpressAd? = null

    override fun getItemType(): Int {
        return mItemType
    }
}

