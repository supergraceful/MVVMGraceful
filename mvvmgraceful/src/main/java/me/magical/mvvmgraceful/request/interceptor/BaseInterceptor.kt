package me.magical.mvvmgraceful.request.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 请求头拦截器
 */
class BaseInterceptor constructor(var mHeaders:Map<String, String>?):Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder=chain.request().newBuilder()
        mHeaders?.let {
            if (it.isNotEmpty()){
                 it.keys.forEach{key->
                    builder.addHeader(key,it[key]!!).build()
                 }
            }
        }
        return chain.proceed(builder.build())
    }
}