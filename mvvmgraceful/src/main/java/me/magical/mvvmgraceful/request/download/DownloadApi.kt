package me.magical.mvvmgraceful.request.download

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface DownloadApi {
    @Streaming
    @GET
    suspend fun downloadFile( @Url fileUrl: String?,@HeaderMap hashMap: HashMap<String,String>?): Response<ResponseBody>


    @Streaming
    @GET
    suspend fun downloadFile( @Url fileUrl: String): Response<ResponseBody>


}