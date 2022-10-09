package com.yang.lib_common.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yang.lib_common.room.entity.MineGoodsDetailData

/**
 * @Author Administrator
 * @ClassName MineGoodsDetailData
 * @Description
 * @Date 2021/8/5 14:28
 */
@Dao
interface MineGoodsDetailDao {

    @Update
    fun updateData(data:MutableList<MineGoodsDetailData>)

    @Query("select * from mine_good_detail")
    fun queryData():MutableList<MineGoodsDetailData>

    @Query("select * from mine_good_detail where type = :type limit (:pageNum-1)*:pageSize,:pageSize")
    fun queryDataByType(type:Int,pageNum:Int,pageSize:Int):MutableList<MineGoodsDetailData>

    @Query("select * from mine_good_detail where id = :id")
    fun queryDataById(id:String):MineGoodsDetailData

    @Insert
    fun insertData(data:MutableList<MineGoodsDetailData>)

    @Insert
    fun insertData(data:MineGoodsDetailData)

    @Query("delete from mine_good_detail where id = :id")
    fun deleteData(id:String)
}