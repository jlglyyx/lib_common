package com.yang.lib_common.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yang.lib_common.room.entity.ImageTypeData

/**
 * @Author Administrator
 * @ClassName ImageTypeDao
 * @Description
 * @Date 2021/8/5 14:28
 */
@Dao
interface ImageTypeDao {

    @Update
    fun updateData(data:MutableList<ImageTypeData>)

    @Query("select * from image_type")
    fun queryData():MutableList<ImageTypeData>

    @Insert
    fun insertData(data:MutableList<ImageTypeData>)
}