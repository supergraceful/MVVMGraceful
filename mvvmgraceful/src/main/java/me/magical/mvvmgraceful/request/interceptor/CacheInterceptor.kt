package me.magical.mvvmgraceful.request.interceptor


import me.magical.mvvmgraceful.utils.NetworkUtil
import me.magical.mvvmgraceful.utils.UtilsBridge
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor constructor(var day: Int = 7) : Interceptor {

    val mContext= UtilsBridge.getApplication()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (NetworkUtil.isNetworkAvailable(mContext)) {
            val proceed = chain.proceed(request)
            val maxAge = 60
            return proceed.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + maxAge)
                .build()
        }else{
            //读取缓存信息
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
            val response = chain.proceed(request)

            val maxStale = 60 * 60 * 24 * day
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
    }
}