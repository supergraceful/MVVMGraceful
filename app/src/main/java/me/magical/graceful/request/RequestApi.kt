package me.magical.graceful.request

import me.magical.graceful.request.bean.*
import okhttp3.RequestBody
import retrofit2.http.*

interface RequestApi {

    @GET("api/sentences")
    suspend fun getNews(): BaseData<NewsBean>

    @GET("api/getMiniVideo")
    suspend fun getMiniVideo(
        @Query("page") page: String,
        @Query("size") size: String
    ): BaseData<MinVideoBean>

    @GET("api/getShortVideo")
    suspend fun getShortVideo(
        @Query("page") page: String,
        @Query("size") size: String
    ): BaseData<MinVideoBean>

    @GET("api/getHaokanVideo")
    suspend fun getHaokanVideo(
        @Query("page") page: String,
        @Query("size") size: String
    ): BaseData<MinVideoBean>

    //type:animal, beauty, car, comic, food, game, movie, person, phone, scenery
    @GET("api/getImages")
    suspend fun getImages(
        @Query("page") page: String,
        @Query("size") size: String,
        @Query("type") type:String?=null
    ): BaseData<TypeImageBean>


    @Headers("Content-Type:application/json")
    @POST("api/login")
    suspend fun login(
        @Body account: RequestBody,
    ): BaseData<LoginBean>

    //    @FormUrlEncoded
//    @POST("api/register")
//    suspend fun register(
//        @Field("account") account: String,
//        @Field("code") count: String,
//        @Field("password") password: String
//    ): BaseData<RegisterBean>
    @Headers("Content-Type:application/json")
    @POST("api/register")
    suspend fun register(
        @Body account: RequestBody,
    ): BaseData<RegisterBean>

    @Headers("Content-Type:application/json")
    @POST("api/sendVerificationCode")
    suspend fun sendVerificationCode(
        @Body account: RequestBody,
    ): BaseData<CaptchaBean>
}