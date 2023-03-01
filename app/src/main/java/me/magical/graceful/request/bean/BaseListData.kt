package me.magical.graceful.request.bean

import me.magical.mvvmgraceful.request.core.BaseBean

class BaseListData<T>(val code: Int, val msg: String, val result: List<T>) : BaseBean<List<T>>() {

    override fun isSuccess(): Boolean = code == 200

    override fun getResponseCode(): Int = code

    override fun getResponseData(): List<T> = result

    override fun getThrowableMessage(): String =msg
}