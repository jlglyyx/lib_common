package com.yang.lib_common.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yang.lib_common.room.entity.VideoTypeData

/**
 * @Author Administrator
 * @ClassName VideoTypeDao
 * @Description
 * @Date 2021/8/5 14:28
 */
@Dao
interface VideoTypeDao {

    @Update
    fun updateData(data:MutableList<VideoTypeData>)

    @Query("select * from video_type")
    fun queryData():MutableList<VideoTypeData>

    @Insert
    fun insertData(data:MutableList<VideoTypeData>)
}