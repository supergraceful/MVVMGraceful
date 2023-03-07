package me.magical.graceful.request

import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.bean.MinVideoBean
import me.magical.graceful.request.bean.WallPaperBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface WallPagerApi {

    @GET
    suspend fun getWallPager(@Url url:String): WallPaperBean
}