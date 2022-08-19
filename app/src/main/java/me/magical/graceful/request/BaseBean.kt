package me.magical.graceful.request

import me.magical.mvvmgraceful.request.core.BaseResponse

class BaseBean<T>(val code: Int, val msg: String, val result: T) : BaseResponse<T>() {


    override fun isSuccess(): Boolean {
        return code == 200
    }

    override fun getResponseCode() = code


    override fun getResponseData() = result

    override fun getThrowableMessage() = msg

}
