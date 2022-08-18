package me.magical.mvvmgraceful.request.core

import android.net.ParseException
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class CustomException : Throwable {

    //错误码
    var code: Int = HttpCode.UNKNOWN

    //异常简略信息
     var msg:String=""

    constructor(code: Int, msg: String?) : super(msg){
        this.code=code
        this.msg=msg?:""
    }

    constructor(code: Int, exception: Exception) : super(exception.message){
        this.code=code
        this.msg=""
    }

    constructor(code: Int, msg: String, throwable: Throwable) : super(throwable){
        this.code=code
        this.msg=msg
    }


    companion object {

        fun handleException(e: Throwable): CustomException {
            return if (e is JsonParseException
                || e is JSONException
                || e is ParseException
            ) {
                CustomException(HttpCode.PARSE_ERROR, "解析错误",e)
            } else if (e is HttpException) {
                var err: String? = ""
                try {
                    err = e.response()!!.errorBody()!!.string()
                } catch (e1: IOException) {
                }
                CustomException(HttpCode.NETWORK_ERROR,"网络错误",e)

            } else if (e is UnknownHostException || e is SocketTimeoutException) {
                //连接错误
                CustomException(HttpCode.NETWORK_ERROR, "连接超时", e)
            } else if (e is ConnectException) {
                CustomException(HttpCode.NETWORK_ERROR, "连接失败", e)
            } else if (e is SSLException) {
                CustomException(HttpCode.SSL_ERROR, "证书验证失败", e)
            } else if (e is ConnectTimeoutException) {
                CustomException(HttpCode.TIMEOUT_ERROR, "连接超时", e)
            } else {
                //未知错误
                CustomException(HttpCode.UNKNOWN, e.message?:"未知异常", e)
            }
        }
    }

    override fun toString(): String {
        return "CustomException(code=$code, message=$msg)"
    }

}