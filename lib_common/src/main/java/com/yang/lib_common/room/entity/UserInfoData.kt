package com.yang.lib_common.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfoData(
    @PrimaryKey
    var id: String,
    var token: String?,
    var userName: String?,
    var userSex: Int?,
    var userAge: Int?,
    var userBirthDay: String?,
    var userDescribe: String?,
    var userAccount: String,
    var userPassword: String,
    var userAddress: String?,
    var userLocationAddress: String?,
    var userLeftBackgroundImage: String?,
    var userInfoBackgroundImage: String?,
    var userMineBackgroundImage: String?,
    var userPhone: String?,
    var userImage: String?,
    var userVipLevel: Int,
    var userVipExpired: Boolean,
    var userType: Int,
    var userObtain: String?,
    var userSign: String?,
    var userExtension: String?,
    var updateTime: String?,
    var createTime: String?,
    var userExtraInfo: String?
)