package me.magical.graceful.request

import retrofit2.http.GET

interface RequestApi {

//    @FormUrlEncoded
//    @POST("api/getWangYiNews")
//    suspend fun getNews(
//        @Field("page") page: String?,
//        @Field("count") count: String?
//    ): BaseBean<List<DtoBean>>?

    @GET("api/sentences")
    suspend fun sentences(): BaseBean<DtoBean>?
}