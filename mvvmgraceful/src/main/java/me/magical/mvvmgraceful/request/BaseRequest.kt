package me.magical.mvvmgraceful.request


import android.text.TextUtils
import me.magical.mvvmgraceful.base.AppManager
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.request.interceptor.BaseInterceptor
import me.magical.mvvmgraceful.request.interceptor.CacheInterceptor
import me.magical.mvvmgraceful.request.interceptor.logging.Level
import me.magical.mvvmgraceful.request.interceptor.logging.LoggingInterceptor
import me.magical.mvvmgraceful.utils.UtilsBridge
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.Exception

open class BaseRequest {

    private lateinit var mOkHttpClient: OkHttpClient

    private lateinit var mRetrofit: Retrofit.Builder

    init {
        val okHttpClient = initOkhttp()
        this.setHttpClientBuilder(okHttpClient)?.run {
            mOkHttpClient = this.build()
        } ?: also {
            mOkHttpClient = okHttpClient.build()
        }

        val retrofitClient = this.initRetrofit()

        this.setRetrofitBuilder(retrofitClient)?.run {
            mRetrofit = this
        } ?: also {

            mRetrofit = retrofitClient
                .addConverterFactory(GsonConverterFactory.create())
        }
    }

    fun  createRetrofit(url: String): Retrofit {
        if (TextUtils.isEmpty(url)){
            throw Exception("url is null")
        }
        return mRetrofit.baseUrl(url).build()
    }

    open fun <T> create(service: Class<T>, url: String): T? {
        return mRetrofit.baseUrl(url).build().create(service)
    }

    private fun initRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .client(mOkHttpClient)
    }

    private fun initOkhttp(): OkHttpClient.Builder {

        return OkHttpClient.Builder().apply {
            //设置缓存配置 缓存最大10M
            cache(Cache(File(UtilsBridge.getApplication().cacheDir, "cxk_cache"), 10 * 1024 * 1024))
            //添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            addInterceptor(BaseInterceptor(setHeader()))
            //添加缓存拦截器 可传入缓存天数，不传默认7天
            addInterceptor(CacheInterceptor())
            //日志拦截器
            addInterceptor(
                LoggingInterceptor.Builder().run {
                    loggable(GLog.isShow)    //是否开启打印
                    setLevel(Level.BASIC)          //打印的等级
                    log(Platform.INFO)             //打印日志类型
                    request("Request")             // request的Tag
                    response("Response")           // Response的Tag
                    addHeader(
                        "log-header",
                        "I am the log request header."
                    ) // 添加打印头, 注意 key 和 value 都不能是中文
                    build()
                }
            )

            //超时时间 连接、读、写
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            //设置线程池
            connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
        }

    }


    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    open fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder?{
        return null
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，Protocol
     */
    open fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder?{
        return null
    }

    /**
     * 设置请求头
     */
    open fun setHeader(): Map<String, String>? {
        return null
    }

}