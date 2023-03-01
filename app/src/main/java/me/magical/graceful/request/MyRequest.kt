package me.magical.graceful.request

import me.magical.mvvmgraceful.request.BaseRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MyRequest private constructor():BaseRequest() {

    val url:String="https://api.apiopen.top"
    var retrofit:Retrofit

    companion object{
        val instance by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){
            MyRequest()
        }
    }

    init {
        retrofit=createRetrofit(url)
    }
    /**
     * 调用Retrofit的create方法获取代理实例
     * @param service 需要代理的 api接口类
     * @param url 请求地址
     * @return 代理接口的对象
     */
    override fun <T> create(service: Class<T>, url: String): T? {
        return super.create(service, url)
    }

    /**
     * 另一种获取代理实例的方式
     */
    fun <T> create(service: Class<T>): T? {
        return retrofit.create(service)
    }
    /**
     * 初始化okhttp，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     * builder为默认实现，可以继续builder添加配置，也可以重新定义一个 OkHttpClient.Builder配置相关参数并返回
     */
    override fun setHttpClientBuilder(): OkHttpClient.Builder? {
        return null
    }

    /**
     * 初始化Retrofit，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，Protocol
     * builder为默认实现，可以继续builder添加配置，也可以重新定义一个Retrofit.Builder配置相关参数并返回
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder? {
        return null
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