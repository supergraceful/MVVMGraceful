package me.magical.graceful.mvvm.model

import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.bean.NewsBean
import me.magical.graceful.request.RequestApi
import me.magical.graceful.request.bean.BaseListData
import me.magical.graceful.request.bean.NewsBean1

class NewsModel{

    private val requestApi:RequestApi = MyRequest.instance.retrofit.create(RequestApi::class.java)

    suspend fun getNews(): BaseData<NewsBean> {
        return requestApi.getNews()
    }
}