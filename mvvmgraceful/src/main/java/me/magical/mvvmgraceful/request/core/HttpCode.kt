package me.magical.mvvmgraceful.request.core

class HttpCode {
    companion object{
        //解析错误
        const val PARSE_ERROR = 10001

        //请求错误
        const val SERVER_ERROR = 10002

        //网络错误
        const val NETWORK_ERROR = 10003

        //证书验证失败
        const val SSL_ERROR = 10004

        //连接超时
        const val TIMEOUT_ERROR = 10005

        //未知错误
        const val UNKNOWN = 99999
    }
}