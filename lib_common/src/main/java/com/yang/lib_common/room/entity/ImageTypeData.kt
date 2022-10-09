package com.yang.lib_common.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *des:
 *
 *@author My Live
 *@date 2021/6/12
 */
@Entity(tableName = "image_type")
data class ImageTypeData(
    @PrimaryKey
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "extra_info")
    var extraInfo: String?
)