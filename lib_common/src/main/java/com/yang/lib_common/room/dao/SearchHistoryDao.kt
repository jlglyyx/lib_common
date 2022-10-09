package com.yang.lib_common.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yang.lib_common.room.entity.SearchData

/**
 * @Author Administrator
 * @ClassName SearchHistory
 * @Description
 * @Date 2021/9/8 15:01
 */
@Dao
interface SearchHistoryDao {
    @Insert
    fun insertData(searchData: SearchData):Long

    @Query("select * from search_history where type = (:type)")
    fun queryAllByType(type:Int):MutableList<SearchData>

    @Delete
    fun deleteData(searchData: SearchData):Int

    @Delete
    fun deleteAllData(mutableList: MutableList<SearchData>):Int
}