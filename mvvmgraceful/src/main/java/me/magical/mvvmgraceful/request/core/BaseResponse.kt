package me.magical.mvvmgraceful.request.core

/**
 * 描述　: 服务器返回数据的基类
 * 如果需要框架帮你做脱壳处理请继承它
 *
 * 必须实现抽象方法，根据自己的业务判断返回请求结果是否成功
 */
abstract class BaseResponse<T> {

    abstract fun isSuccess():Boolean

    abstract fun getResponseCode():Int

    abstract fun getResponseData():T

    abstract fun getThrowableMessage():String
}