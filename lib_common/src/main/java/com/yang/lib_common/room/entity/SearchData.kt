package com.yang.lib_common.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author Administrator
 * @ClassName SearchData
 * @Description
 * @Date 2021/9/8 14:56
 */
@Entity(tableName = "search_history")
data class SearchData(
    @PrimaryKey
    var id: String,
    var type: Int,
    var content: String,
    var createTime:String,
    var updateTime:String
)
