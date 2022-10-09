package com.yang.lib_common.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yang.lib_common.app.BaseApplication
import com.yang.lib_common.room.dao.*
import com.yang.lib_common.room.entity.*
import java.io.File


/**
 * @Author Administrator
 * @ClassName BaseAppDate
 * @Description
 * @Date 2021/8/5 14:22
 */
@Database(entities = [UserInfoData::class,ImageTypeData::class,VideoTypeData::class,SearchData::class, ImageDataItem::class, VideoDataItem::class,UploadTaskData::class,MineGoodsDetailData::class],version = 1,exportSchema = true)
abstract class BaseAppDatabase : RoomDatabase() {

    abstract fun userInfoDao(): UserInfoDao

    abstract fun imageTypeDao(): ImageTypeDao

    abstract fun videoTypeDao(): VideoTypeDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun imageDataDao(): ImageDataDao

    abstract fun videoDataDao(): VideoDataDao

    abstract fun uploadTaskDao(): UploadTaskDao

    abstract fun mineGoodsDetailDao(): MineGoodsDetailDao

    companion object {
        val instance: BaseAppDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            getBaseAppDatabase()
        }
        private fun getBaseAppDatabase(): BaseAppDatabase {
            return Room.databaseBuilder(
                BaseApplication.baseApplication,
                BaseAppDatabase::class.java,
                "${createFile()}/app.db"
            ).build()
        }

        private fun createFile():String{
            val file = File("${BaseApplication.baseApplication.cacheDir}/app_/db_")
            if (!file.exists()){
                file.mkdirs()
            }
            return file.path
        }
//        private fun createFile():String{
//            val file = File("${Environment.getExternalStorageDirectory()}/MFiles/db")
//            if (!file.exists()){
//                file.mkdirs()
//            }
//            return file.path
//        }
    }


}