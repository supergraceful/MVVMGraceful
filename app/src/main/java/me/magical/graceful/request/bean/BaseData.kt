package me.magical.graceful.request.bean

import me.magical.mvvmgraceful.request.core.BaseBean

class BaseData<T>(val code: Int, val message: String, val result: T) : BaseBean<T>() {

    override fun isSuccess(): Boolean = code == 200

    override fun getResponseCode(): Int = code

    override fun getResponseData(): T? = result

    override fun getThrowableMessage(): String =message
}