package me.magical.graceful.mvvm.model

import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.bean.NewsBean
import me.magical.graceful.request.RequestApi


class NewsModel{

    private val requestApi:RequestApi = MyRequest.instance.create(RequestApi::class.java)

    suspend fun getNews(): BaseData<NewsBean> {
        return requestApi.getNews()
    }
}