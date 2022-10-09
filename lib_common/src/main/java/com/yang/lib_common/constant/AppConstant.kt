package com.yang.lib_common.constant

interface AppConstant {

    object ClientInfo {

        const val BASE_IP = "http://192.168.81.200"

        const val BASE_PORT = "20000"

        const val BASE_URL = "https://www.baidu.com/"
//        const val BASE_URL = "https://www.wanandroid.com/"
//        const val BASE_URL = "http://jlgl.free.idcfengye.com/"
//        const val BASE_URL = "http://10.16.242.28:20000/"

        const val BASE_WEB_URL = "http://192.168.174.197:8080/#"


        const val TAG = "RemoteModule"

        const val TAG_LOG = "httpLog"

        const val CONNECT_TIMEOUT = 1000L

        const val READ_TIMEOUT = 1000L

        const val WRITE_TIMEOUT = 1000L

        const val CONTENT_TYPE = "multipart/form-data"

        const val UTF_8 = "UTF-8"
    }

    object RoutePath {
        private const val ACTIVITY = "activity"
        private const val FRAGMENT = "fragment"
        private const val SERVICE = "service"
        private const val PROVIDE = "provide"

        const val MODULE_MAIN = "module_main"
        const val MODULE_MINE = "module_mine"
        const val MODULE_VIDEO = "module_video"
        const val MODULE_PICTURE = "module_picture"
        const val MODULE_LOGIN = "module_login"
        const val MODULE_TASK = "module_task"

        /**
         * main
         */

        const val MAIN_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/MainActivity"
        const val ADD_TASK_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/AddTaskActivity"
        const val MY_COLLECTION_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/MyCollectionActivity"
        const val MY_PUSH_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/MyPushActivity"
        const val TASK_DETAIL_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/TaskDetailActivity"
        const val PICTURE_SELECT_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/PictureSelectActivity"
        const val OPEN_VIP_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/OpenVipActivity"
        const val PRIVACY_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/PrivacyActivity"
        const val ACCESSIBILITY_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/AccessibilityActivity"

        const val ACCOUNT_SETTING_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/AccountSettingActivity"
        const val DOWNLOAD_SETTING_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/DownloadSettingActivity"
        const val ABOUT_AND_HELP_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/AboutAndHelpActivity"
        const val WEB_VIEW_ACTIVITY = "/$MODULE_MAIN/$ACTIVITY/WebViewActivity"



        const val MAIN_FRAGMENT = "/$MODULE_MAIN/$FRAGMENT/MainFragment"
        const val MAIN_LEFT_FRAGMENT = "/$MODULE_MAIN/$FRAGMENT/MainLeftFragment"
        const val MAIN_RIGHT_FRAGMENT = "/$MODULE_MAIN/$FRAGMENT/MainRightFragment"
        const val LEFT_FRAGMENT = "/$MODULE_MAIN/$FRAGMENT/LeftFragment"
        const val MY_COLLECTION_FRAGMENT = "/$MODULE_MAIN/$FRAGMENT/MyCollectionFragment"
        const val MY_DOWNLOAD_FRAGMENT = "/$MODULE_MAIN/$FRAGMENT/MyDownLoadFragment"

        /**
         * task
         */
        const val TASK_FRAGMENT = "/$MODULE_TASK/$FRAGMENT/TaskFragment"

        const val TASK_SELLER_PROGRESS_ACTIVITY = "/$MODULE_TASK/$ACTIVITY/TaskSellerProgressActivity"
        const val TASK_BUYER_PROGRESS_ACTIVITY = "/$MODULE_TASK/$ACTIVITY/TaskBuyerProgressActivity"


        /**
         * picture
         */

        const val PICTURE_FRAGMENT = "/$MODULE_PICTURE/$FRAGMENT/PictureFragment"
        const val PICTURE_ITEM_FRAGMENT = "/$MODULE_PICTURE/$FRAGMENT/PictureItemFragment"

        const val PICTURE_ITEM_ACTIVITY = "/$MODULE_PICTURE/$ACTIVITY/PictureItemActivity"
        const val PICTURE_SEARCH_ACTIVITY = "/$MODULE_PICTURE/$ACTIVITY/SearchActivity"
        const val PICTURE_UPLOAD_ACTIVITY = "/$MODULE_PICTURE/$ACTIVITY/PictureUploadActivity"

        /**
         * login
         */
        const val SPLASH_ACTIVITY = "/$MODULE_LOGIN/$ACTIVITY/SplashActivity"
        const val LOGIN_ACTIVITY = "/$MODULE_LOGIN/$ACTIVITY/LoginActivity"
        const val REGISTER_ACTIVITY = "/$MODULE_LOGIN/$ACTIVITY/RegisterActivity"
        const val CONNECT_ADDRESS_ACTIVITY = "/$MODULE_LOGIN/$ACTIVITY/ConnectAddressActivity"
        const val USER_TYPE_SELECT_ACTIVITY = "/$MODULE_LOGIN/$ACTIVITY/UserTypeSelectActivity"

        /**
         * video
         */
        const val VIDEO_FRAGMENT = "/$MODULE_VIDEO/$FRAGMENT/VideoFragment"
        const val VIDEO_ITEM_FRAGMENT = "/$MODULE_VIDEO/$FRAGMENT/VideoItemFragment"


        const val ADVERTISEMENT_VIDEO_ACTIVITY = "/$MODULE_VIDEO/$ACTIVITY/AdvertisementVideoActivity"
        const val VIDEO_MAIN_ACTIVITY = "/$MODULE_VIDEO/$ACTIVITY/MainActivity"

        const val VIDEO_ITEM_ACTIVITY = "/$MODULE_VIDEO/$ACTIVITY/VideoItemActivity"
        const val VIDEO_SEARCH_ACTIVITY = "/$MODULE_VIDEO/$ACTIVITY/SearchActivity"

        const val VIDEO_UPLOAD_ACTIVITY = "/$MODULE_VIDEO/$ACTIVITY/VideoUploadActivity"

        const val VIDEO_UPLOAD_TASK_ACTIVITY = "/$MODULE_VIDEO/$ACTIVITY/VideoUploadTaskActivity"

        const val VIDEO_SCREEN_ACTIVITY = "/$MODULE_VIDEO/$ACTIVITY/VideoScreenActivity"

        /**
         * mine
         */
        const val MINE_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MainActivity"

        const val MINE_USER_INFO_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineUserInfoActivity"

        const val MINE_CHANGE_USER_INFO_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineChangeUserInfoActivity"

        const val MINE_VIEW_HISTORY_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineViewHistoryActivity"

        const val MINE_OBTAIN_TURNOVER_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineObtainTurnoverActivity"

        const val MINE_OBTAIN_EXCHANGE_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineObtainExchangeActivity"

        const val MINE_EXCHANGE_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineExchangeActivity"

        const val MINE_GOODS_DETAIL_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineGoodsDetailActivity"

        const val MINE_SIGN_TURNOVER_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineSignTurnoverActivity"

        const val MINE_EXTENSION_TURNOVER_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineExtensionTurnoverActivity"

        const val MINE_EXTENSION_QR_CODE_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineExtensionQRCodeActivity"

        const val MINE_ADDRESS_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineAddressActivity"

        const val MINE_CHANGE_PASSWORD_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineChangePasswordActivity"

        const val MINE_CHANGE_PHONE_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineChangePhoneActivity"

        const val MINE_ADD_ADDRESS_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineAddAddressActivity"

        const val MINE_OBTAIN_TASK_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineObtainTaskActivity"

        const val MINE_ORDER_DETAIL_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineOrderDetailActivity"

        const val MINE_CREATE_ORDER_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineCreateOrderActivity"

        const val MINE_SETTING_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MineSettingActivity"

        const val MINE_WEB_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/WebActivity"

        const val MINE_ABOUT_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/AboutActivity"

        const val MINE_MY_BALANCE_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MyBalanceActivity"

        const val MINE_MY_RIGHTS_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MyRightsActivity"

        const val MINE_MY_INFO_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/MyInfoActivity"

        const val MINE_TASK_HISTORY_ACTIVITY = "/$MODULE_MINE/$ACTIVITY/TaskHistoryActivity"

        const val MINE_FRAGMENT = "/$MODULE_MINE/$FRAGMENT/MineFragment"

        const val MINE_EXCHANGE_STATUS_FRAGMENT = "/$MODULE_MINE/$FRAGMENT/MineExchangeStatusFragment"




    }

    object Constant {

        const val LOGIN_STATUS = "login_status"

        const val LOGIN_SUCCESS = 200

        const val LOGIN_FAIL = 202

        const val LOGIN_NO_PERMISSION = 201

        const val CLICK_TIME: Long = 1000

        const val SPLASH_VIDEO_URL = "http://192.168.31.60:8080/AdobePhotoshopCS6.rar"
        const val SPLASH_VIDEO_URLS = "http://192.168.31.60:8080/ps2020pjb.rar"

        const val SPLASH_VIDEO_PATH = "/storage/emulated/0/A/splash.mp4"

        const val ID = "id"

        const val TOKEN = "token"

        const val URL = "url"

        const val TITLE = "title"

        const val TYPE = "type"

        const val DATA = "data"

        const val NAME = "name"

        const val NUM = "num"

        const val CONTENT = "content"

        const val PAGE_SIZE_COUNT = 10

        const val TAB_HEIGHT = "TAB_HEIGHT"

        const val LOGIN_INFO = "login_info"

        const val USER_INFO = "user_info"

        const val LOGIN_USER_TYPE = "login_user_type"

        const val USER_ID = "userId"

        const val PAGE_SIZE = "pageSize"

        const val PAGE_NUMBER = "pageNum"

        const val KEYWORD = "keyword"

        const val LOCATION = "location"

        const val PHONE = "phone"

        const val PASSWORD = "password"

        const val VERIFICATION = "verification"

        const val NUM_MINUS_ONE = -1

        const val NUM_ZERO = 0

        const val NUM_ONE = 1

        const val NUM_TWO = 2

        const val NUM_THREE = 3

        const val NUM_FOUR = 4

        const val NUM_FIVE = 5

        const val NUM_SIX = 6

        const val NUM_SEVEN = 7

        const val IP = "ip"

        const val PORT = "port"

        const val FONT_TYPE = "fontType"

        const val VIDEO = "video"

        const val PICTURE = "picture"

        const val COLLECT = "collect"

        const val DOWNLOAD = "download"

        const val COMMENT = "comment"

        const val BUNDLE = "bundle"

        const val ITEM_CONTENT = 0
        const val ITEM_AD = 1

        const val ITEM_MAIN_TITLE = 0
        const val ITEM_MAIN_CONTENT_TEXT = 1
        const val ITEM_MAIN_CONTENT_IMAGE = 2
        const val ITEM_MAIN_IDENTIFICATION = 3
        const val ITEM_MAIN_CONTENT_VIDEO = 4

        /**
         * 主页视频布局类型
         */
        const val ITEM_VIDEO_RECOMMEND_TYPE = 0
        const val ITEM_VIDEO_SMART_IMAGE = 1
        const val ITEM_VIDEO_BIG_IMAGE = 2

        /**
         * 评论布局类型
         */
        const val PARENT_COMMENT_TYPE = 0

        const val CHILD_COMMENT_TYPE = 1

        const val CHILD_REPLY_COMMENT_TYPE = 2
    }


    object LoadingViewEnum {

        const val EMPTY_VIEW = 0

        const val ERROR_VIEW = 1
    }

    object NoticeChannel{
        const val DOWNLOAD = "download"
    }
}