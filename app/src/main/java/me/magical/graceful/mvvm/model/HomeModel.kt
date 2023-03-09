package me.magical.graceful.mvvm.model

import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.RequestApi
import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.bean.MinVideoBean
import me.magical.graceful.request.bean.TypeImageBean
import javax.inject.Inject


class HomeModel @Inject constructor() {
    private val requestApi: RequestApi = MyRequest.instance.create(RequestApi::class.java)

    suspend fun getImages(page:Int,size:Int=10): BaseData<TypeImageBean> {
        return requestApi.getImages(page.toString(),size.toString())
    }
}