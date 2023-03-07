package me.magical.graceful.mvvm.model

import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.RequestApi
import me.magical.graceful.request.WallPagerApi
import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.bean.NewsBean
import me.magical.graceful.request.bean.WallPaperBean

class WallPagerModel {

    private val requestApi = MyRequest.instance.create("http://service.picasso.adesk.com",WallPagerApi::class.java)

    suspend fun getWallPager(page:Int): WallPaperBean {

        val url="/v1/vertical/vertical?limit=30&skip=180&adult=false&first=${page}&order=hot"
        return requestApi.getWallPager(url)
    }
}