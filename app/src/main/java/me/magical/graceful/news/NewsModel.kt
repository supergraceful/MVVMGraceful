package me.magical.graceful.news

import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.BaseBean
import me.magical.graceful.request.DtoBean
import me.magical.graceful.request.RequestApi

class NewsModel {

    suspend fun getNews(page:String,count:String): BaseBean<DtoBean>? {
        return MyRequest.instance.create(RequestApi::class.java)?.sentences()
    }
}