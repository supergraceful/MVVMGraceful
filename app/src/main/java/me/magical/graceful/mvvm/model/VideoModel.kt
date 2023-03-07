package me.magical.graceful.mvvm.model

import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.RequestApi
import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.bean.MinVideoBean

class VideoModel {
    private val requestApi: RequestApi = MyRequest.instance.create(RequestApi::class.java)

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