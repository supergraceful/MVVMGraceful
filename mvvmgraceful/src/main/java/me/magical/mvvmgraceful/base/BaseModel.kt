package me.magical.mvvmgraceful.base

import me.magical.mvvmgraceful.request.core.BaseResponse
import me.magical.mvvmgraceful.request.core.CustomException

open class BaseModel {

    /**
     * 根据状态返回，model层再不需要次方法时可以不继承
     */
    suspend inline fun <reified T> launchRequest(
        noinline block: suspend () -> BaseResponse<T>
    ): DataResult<T> {
        return try {
            val block = block()
            if (block.isSuccess()) {
                DataResult.OnSuccess(block.getData())
            } else {
                DataResult.OnError(
                    CustomException(
                        block.getCode(),
                        block.getMessage() ?: "未知异常"
                    )
                )
            }
        } catch (e: Exception) {
            DataResult.OnError(CustomException.handleException(e))
        }
    }


}