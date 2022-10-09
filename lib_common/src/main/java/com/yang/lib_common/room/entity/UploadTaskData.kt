package com.yang.lib_common.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author Administrator
 * @ClassName UploadTaskData
 * @Description
 * @Date 2021/12/14 10:27
 *
 * status 0上传中 1完成 2暂停
 */
@Entity(tableName = "upload_task")
data class UploadTaskData (@PrimaryKey var id:String,var progress:Int,var filePath:String,var status:Int,var currentSize:Long,var countSize:Long)