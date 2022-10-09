package com.yang.lib_common.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yang.lib_common.room.entity.ImageDataItem

/**
 * @Author Administrator
 * @ClassName ImageDataDao
 * @Description
 * @Date 2021/12/10 15:15
 */
@Dao
interface ImageDataDao {
    @Update
    fun updateData(data:MutableList<ImageDataItem>)

    @Query("select * from image_data")
    fun queryData():MutableList<ImageDataItem>

    @Query("select * from image_data where imageType = :imageType limit (:pageNum-1)*:pageSize,:pageSize")
    fun queryDataByType(imageType:String,pageNum:Int,pageSize:Int):MutableList<ImageDataItem>

    @Insert
    fun insertData(data:MutableList<ImageDataItem>)
}