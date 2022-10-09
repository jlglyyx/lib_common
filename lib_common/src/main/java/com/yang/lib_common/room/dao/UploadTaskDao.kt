package com.yang.lib_common.room.dao

import androidx.room.*
import com.yang.lib_common.room.entity.UploadTaskData

/**
 * @Author Administrator
 * @ClassName VideoTypeDao
 * @Description
 * @Date 2021/8/5 14:28
 */
@Dao
interface UploadTaskDao {

    @Update
    fun updateData(data:MutableList<UploadTaskData>)

    @Update
    fun updateData(data:UploadTaskData)

    @Query("select * from upload_task")
    fun queryData():MutableList<UploadTaskData>

    @Query("select * from upload_task where id = :id")
    fun queryData(id:String):UploadTaskData

    @Insert
    fun insertData(data:MutableList<UploadTaskData>)

    @Insert
    fun insertData(data:UploadTaskData)

    @Delete
    fun deleteData(data:UploadTaskData)

    @Delete
    fun deleteData(data:MutableList<UploadTaskData>)

    @Query("DELETE FROM upload_task")
    fun deleteData()
}