package me.magical.graceful.request

import me.magical.mvvmgraceful.request.BaseRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MyRequest private constructor() : BaseRequest() {

    var url: String = "https://api.apiopen.top"

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MyRequest()
        }
    }

    fun settingUrl(type: String) {
        when (type) {
            "openapi" -> {
                url = "https://api.apiopen.top"
            }
            "bing" -> {
                url = "https://cn.bing.com"
            }
            "picasso" -> {
                url = "http://service.picasso.adesk.com"
            }
        }
    }

    /**
     * 调用Retrofit的create方法获取代理实例
     * @param service 需要代理的 api接口类
     * @param url 请求地址
     * @return 代理接口的对象
     */
    override fun <T> create(url: String, service: Class<T>): T {
        return super.create(url, service)
    }

    /**
     * 另一种获取代理实例的方式
     */
    fun <T> create(service: Class<T>): T {
        return super.create(url, service)
    }

    /**
     * Init okhttp builder
     *
     * @return
     */
    override fun initOkhttpBuilder(): OkHttpClient.Builder {
        return super.initOkhttpBuilder()
    }

    /**
     * Init retrofit builder
     *
     * @return
     */
    override fun initRetrofitBuilder(): Retrofit.Builder {
        return super.initRetrofitBuilder()
    }

    /**
     * 添加统一的请求头,
     * 当setHttpClientBuilder重写并使用的是默认的builder的配置时可以使用该方法添加统一请求头，
     * 如果setHttpClientBuilder重写并重新定义了OkHttpClient.Builder改方法失效
     */
    override fun setHeader(): Map<String, String>? {
        return null
    }
}