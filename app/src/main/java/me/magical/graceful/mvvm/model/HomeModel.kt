package me.magical.graceful.mvvm.model

import dagger.hilt.android.scopes.ViewModelScoped
import me.magical.graceful.request.JavaUtils
import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.RequestApi
import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.bean.MinVideoBean
import me.magical.graceful.request.bean.TypeImageBean
import me.magical.mvvmgraceful.ext.GLog
import javax.inject.Inject
import javax.inject.Named

@ViewModelScoped
class HomeModel @Inject constructor() {
//    private val requestApi: RequestApi = MyRequest.instance.create(RequestApi::class.java)

    @Inject
    @Named("apiOpen")
    lateinit var requestApi: RequestApi

    suspend fun getImages(page:Int,size:Int=10): BaseData<TypeImageBean> {
        val test=MyRequest.instance

        GLog.i("HomeModel中hashCode值： ${requestApi.hashCode()}")
        GLog.i("HomeModel 测试 hashCode值 ${test.hashCode()}")
        return requestApi.getImages(page.toString(),size.toString())
    }
}