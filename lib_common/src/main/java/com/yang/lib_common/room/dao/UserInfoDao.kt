package com.yang.lib_common.room.dao

import androidx.room.*
import com.yang.lib_common.room.entity.UserInfoData

/**
 * @Author Administrator
 * @ClassName UserInfoDao
 * @Description
 * @Date 2021/8/5 14:28
 */
@Dao
interface UserInfoDao {

    @Update
    fun updateData(data:MutableList<UserInfoData>)

    @Update
    fun updateData(data:UserInfoData)

    @Query("select * from user_info")
    fun queryData():MutableList<UserInfoData>

    @Query("select * from user_info where id = :id")
    fun queryData(id:String):UserInfoData

    @Query("select * from user_info where userAccount = :userAccount")
    fun queryDataByAccount(userAccount:String):UserInfoData

    @Insert
    fun insertData(data:MutableList<UserInfoData>)

    @Insert
    fun insertData(data:UserInfoData)

    @Delete
    fun deleteData(data:UserInfoData)

    @Delete
    fun deleteData(data:MutableList<UserInfoData>)

    @Query("DELETE FROM user_info")
    fun deleteData()
}