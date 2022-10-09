package com.yang.lib_common.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author Administrator
 * @ClassName MineGoodsDetailData
 * @Description
 * @Date 2022/1/4 11:54
 */
@Entity(tableName = "mine_good_detail")
data class MineGoodsDetailData(@PrimaryKey var id: String, var type: Int, var content: String)