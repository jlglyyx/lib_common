package com.yang.lib_common.handle


/**
 * @ClassName ErrorCode
 *
 * @Description
 *
 * @Author 1
 *
 * @Date 2020/11/26 17:57
 */

interface IHttpException {

    enum class HttpException(var code: Int,var  message: String){

        NO_FIND(404,"资源未找到"),

        NO_JURISDICTION(401,"登陆失效,请重新登陆")

    }
    enum class OtherException(var message: String){

        SOCKET_TIME_OUT_ERROR("连接超时,稍后重试"),

        NETWORK_ERROR("网络异常,稍后重试"),

        NO_NETWORK_ERROR("网络未连接"),

        JSON_SYNTAX_ERROR("Json解析异常"),

        UN_KNOWN_ERROR("出了点小错误,请等待修复哦")
    }

}
