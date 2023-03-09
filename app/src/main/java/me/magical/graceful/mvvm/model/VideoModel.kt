package me.magical.graceful.mvvm.model

import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.RequestApi
import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.bean.MinVideoBean
import javax.inject.Inject
import javax.inject.Named

class VideoModel  @Inject constructor(){

    @Inject
    @Named("apiOpen")
    lateinit var requestApi: RequestApi

    suspend fun getMiniVideo(page:Int,size:Int=10): BaseData<MinVideoBean> {
        return requestApi.getMiniVideo(page.toString(),size.toString())
    }

    suspend fun getHaokanVideo(page:Int,size:Int=10): BaseData<MinVideoBean> {
        return requestApi.getHaokanVideo(page.toString(),size.toString())
    }

    suspend fun getShortVideo(page:Int,size:Int=10): BaseData<MinVideoBean> {
        return requestApi.getShortVideo(page.toString(),size.toString())
    }
}