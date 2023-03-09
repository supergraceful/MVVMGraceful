package me.magical.graceful.mvvm.model

import me.magical.graceful.request.JavaUtils
import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.bean.NewsBean
import me.magical.graceful.request.RequestApi
import me.magical.mvvmgraceful.ext.GLog
import javax.inject.Inject
import javax.inject.Named


class NewsModel @Inject constructor(){

//    private val requestApi:RequestApi = MyRequest.instance.create(RequestApi::class.java)

    @Inject
    @Named("apiOpen")
    lateinit var requestApi: RequestApi

    suspend fun getNews(): BaseData<NewsBean> {
        return requestApi.getNews()
    }
}