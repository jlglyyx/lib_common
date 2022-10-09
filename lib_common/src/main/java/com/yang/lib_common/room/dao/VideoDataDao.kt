package com.yang.lib_common.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yang.lib_common.room.entity.VideoDataItem

/**
 * @Author Administrator
 * @ClassName ImageDataDao
 * @Description
 * @Date 2021/12/10 15:15
 */
@Dao
interface VideoDataDao {
    @Update
    fun updateData(data:MutableList<VideoDataItem>)

    @Query("select * from video_data")
    fun queryData():MutableList<VideoDataItem>

    @Query("select * from video_data where videoType = :videoType limit (:pageNum-1)*:pageSize,:pageSize")
    fun queryDataByType(videoType:String,pageNum:Int,pageSize:Int):MutableList<VideoDataItem>

    @Query("select * from video_data where id = :sid")
    fun queryDataBySid(sid:String):MutableList<VideoDataItem>

    @Insert
    fun insertData(data:MutableList<VideoDataItem>)
}