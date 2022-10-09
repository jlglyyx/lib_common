@file:JvmName("AppUtils")

package com.yang.lib_common.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.google.gson.Gson
import com.jakewharton.rxbinding4.view.clicks
import com.tencent.mmkv.MMKV
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.constant.AppConstant.Constant.CLICK_TIME
import com.yang.lib_common.room.entity.UserInfoData
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


private const val TAG = "AppUtils"

val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

val formatDate_YYYYMMMDDHHMMSS = SimpleDateFormat("yyyyMMddHHmmss")

val formatDate_YYYY_MMM_DD_HHMMSS = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

/**
 * @return 宽高集合
 */
fun getScreenPx(context: Context): IntArray {
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels
    val heightPixels = displayMetrics.heightPixels
    return intArrayOf(widthPixels, heightPixels)
}

/**
 * @return 宽高集合
 */
fun getScreenDpi(context: Context): FloatArray {
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val widthPixels = displayMetrics.xdpi
    val heightPixels = displayMetrics.ydpi
    return floatArrayOf(widthPixels, heightPixels)
}

/**
 * @return 获取状态栏高度
 */
fun getStatusBarHeight(context: Context): Int {
    val resources = context.resources
    val identifier = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(identifier)
}

/**
 * @return 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun Float.dip2px(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * @return 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Float.px2dip(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * view点击添加防抖动
 */
fun View.clicks(): Observable<Unit> {
    return this.clicks().throttleFirst(CLICK_TIME, TimeUnit.MILLISECONDS)
}


/**
 * @return toJson
 */
fun Any.toJson(): String {
    return Gson().toJson(this)
}


/**
 * @return 解析Json
 */
fun <T> fromJson(json: String, t: Class<T>): T {
    return Gson().fromJson<T>(json, t)
}

/**
 * @return 获取文件夹下所有文件路径
 */
fun getFilePath(
    path: String = "${Environment.getExternalStorageDirectory()}/MFiles/picture",
    mutableListOf: MutableList<String> = mutableListOf()
): MutableList<String> {
    //val file = File("${Environment.getExternalStorageDirectory()}/DCIM/Camera")
    val file = File(path)
    if (file.isDirectory) {
        val listFiles = file.listFiles()
        listFiles?.let {
            for (mFiles in listFiles) {
                if (mFiles.isDirectory) {
                    getFilePath(mFiles.absolutePath, mutableListOf)
                } else {
                    mutableListOf.add(mFiles.absolutePath)
                }

            }
        }
    } else {
        mutableListOf.add(file.absolutePath)
    }

    return mutableListOf
}

fun MutableList<String>.filterEmptyFile():MutableList<String>{
    return this.filterNot {
        val file = File(it)
        !file.exists()
    }.toMutableList()
}

/**
 * @return 获取文件夹下所有文件夹路径
 */
fun getDirectoryName(
    path: String = "${Environment.getExternalStorageDirectory()}/MFiles/picture/A",
    mutableListOf: MutableList<File> = mutableListOf()
): MutableList<File> {
    val file = File(path)
    if (file.isDirectory) {
        val listFiles = file.listFiles()
        listFiles?.forEach {
            if (it.isDirectory) {
                getDirectoryName(it.path, mutableListOf)
                Log.i(TAG, "getDirectoryName: ${it.name}  ${it.path}")
                mutableListOf.add(it)
            }
        }
    }
    return mutableListOf
}


/**
 * @return 获取getDefaultMMKV
 */
fun getDefaultMMKV(): MMKV {
    return MMKV.defaultMMKV()
}

/**
 * @return 获取用户缓存
 */
fun getUserInfo(): UserInfoData? {
    val userInfo = getDefaultMMKV().decodeString(AppConstant.Constant.USER_INFO, "")
    if (!userInfo.isNullOrEmpty()) {
        return Gson().fromJson<UserInfoData>(userInfo, UserInfoData::class.java)
    }
    return null
}
/**
 * @return 更新用户缓存
 */
fun updateUserInfo(userInfoData:UserInfoData) {
    getDefaultMMKV().encode(AppConstant.Constant.USER_INFO, userInfoData.toJson())
}

/**
 * @return 返回xx,xx
 */
fun MutableList<String>.formatWithSymbol(symbol: String = ","): String {
    val stringBuilder = StringBuilder()
    this.forEachIndexed { index, s ->
        if (index == this.size - 1) {
            stringBuilder.append(s)
        } else {
            stringBuilder.append(s).append(symbol)
        }
    }
    return stringBuilder.toString()
}

/**
 * @return 解析xx,xx
 */
fun String.symbolToList(symbol: String = ","): MutableList<String> {
    val mutableListOf = mutableListOf<String>()
    val split = this.split(symbol)
    mutableListOf.addAll(split)
    return mutableListOf
}

/**
 * 不行 content://com.android.externalstorage.documents/document/primary%3AMFiles%2Fpicture%2F1638856053728_a.jpg
 * 可以 content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FPictures%2F20211223_092453.jpg
 * @return uri2path
 */
fun uri2path(context: Context, uri: Uri): String {
    var path = ""
    val contentResolver = context.contentResolver
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val query =
        contentResolver.query(uri, projection, null, null, null)
    try {
        query?.let {
            val columnIndex = query.getColumnIndex(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            path = query.getString(columnIndex)
            Log.i(TAG, "uri2path: $path")
            query.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.i(TAG, "uri2path: ${e.message}")
    }

    return path
}

/**
 * @return 获取app VersionCode
 */
fun getVersionCode(context: Context): Int {
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionCode

}

/**
 * @return 获取app VersionName
 */
fun getVersionName(context: Context): String {
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName
}

/**
 * @return 获取指定文件大小
 */
fun getAllFileSize(file: File): Long {
    var size = 0L
    if (file.isDirectory) {
        val listFiles = file.listFiles()
        listFiles?.let {
            if (listFiles.isEmpty()) {
                return 0
            }
            for (mFile in listFiles) {
                size += if (mFile.isDirectory) {
                    getAllFileSize(mFile)
                } else {
                    mFile.length()
                }
            }
        }
    } else {
        size = file.length()
    }
    return size
}

/**
 * @return 格式化文件大小格式
 */
fun formatSize(size: Long): String {
    Log.i(TAG, "formatSize: $size")

    val k = size.toFloat() / 1024
    if (k < 1) {
        return "0K"
    }
    val m = k / 1024

    if (m < 1) {
        return DecimalFormat("0.00K").format(k)
    }

    val g = m / 1024

    if (g < 1) {
        return DecimalFormat("0.00M").format(m)
    }

    val t = g / 1024

    if (t < 1) {
        return DecimalFormat("0.00G").format(g)
    }
    val t1 = t / 1024

    if (t1 < 1) {
        return DecimalFormat("0.00G").format(t)
    }
    return DecimalFormat("0.00T").format(t1)
}

/**
 * @return 删除文件夹
 */
fun deleteDirectory(file: File) {
    try {
        if (file.isDirectory) {
            file.listFiles()?.let {
                if (it.isNotEmpty()) {
                    for (mFile in it) {
                        if (mFile.isDirectory) {
                            deleteDirectory(file)
                        } else {
                            deleteFile(mFile)
                        }
                    }
                }
            }
        } else {
            deleteFile(file)
        }
    }catch (e:Exception){
        e.printStackTrace()
    }

}

/**
 * @return 删除文件
 */
fun deleteFile(file: File) {
    if (file.exists()) {
        file.delete()
    }
}


/**
 * 跳转登录页
 */
fun buildARouterLogin(mContext: Context){
    buildARouter(AppConstant.RoutePath.LOGIN_ACTIVITY)
        .withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.bottom_in, R.anim.bottom_out))
        .withInt(AppConstant.Constant.DATA,0)
        .navigation(mContext)
}

/**
 * 是否是手机号
 */
fun String.isPhone():Boolean{
    val pattern = Pattern.compile("^1[0-9]{10}")
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

/**
 *
 */
fun toCloseAd(vipLevel:Int):Boolean{
    val userInfo = getUserInfo()
    userInfo?.let {
        /*如果过期了返回*/
        if (it.userVipExpired){
            return false
        }
        if (it.userVipLevel >= vipLevel){
            return true
        }
    }
    return false
}

