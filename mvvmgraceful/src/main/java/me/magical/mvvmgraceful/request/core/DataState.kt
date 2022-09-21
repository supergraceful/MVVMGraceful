package me.magical.mvvmgraceful.request.core

import com.google.gson.annotations.Until

/**
 * 返回请求的响应不同的状态值
 */
sealed class DataState<out T> {

    //请求开始
    data class OnStart(val message:String) : DataState<Nothing>()

    //请求成功
    data class OnSuccess<out T>(val data: T) : DataState<T>()

    //请求失败
    data class OnError(val exception: CustomException) : DataState<Nothing>()

    //请求完成（无论成功失败都会）
    object OnComplete : DataState<Nothing>()
}


