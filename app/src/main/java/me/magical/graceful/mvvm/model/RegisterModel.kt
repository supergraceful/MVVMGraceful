package me.magical.graceful.mvvm.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import me.magical.graceful.request.MyRequest
import me.magical.graceful.request.RequestApi
import me.magical.graceful.request.bean.BaseData
import me.magical.graceful.request.bean.CaptchaBean
import me.magical.graceful.request.bean.LoginBean
import me.magical.graceful.request.bean.RegisterBean
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject


class RegisterModel @Inject constructor() {

    private val requestApi: RequestApi = MyRequest.instance.create(RequestApi::class.java)

    suspend fun sendVerificationCode(mail: String): BaseData<CaptchaBean> {

        val json = JsonObject()
        json.addProperty("mail", mail)
        val requestBody1 = RequestBody.create("text/plain".toMediaTypeOrNull(), json.toString())
        return requestApi.sendVerificationCode(requestBody1)
    }

    suspend fun register(account: String, code: String, password: String): BaseData<RegisterBean> {
        val json = JsonObject()
        json.addProperty("account", account)
        json.addProperty("code", code)
        json.addProperty("password", password)
        val requestBody1 = RequestBody.create("text/plain".toMediaTypeOrNull(), json.toString())
        return requestApi.register(requestBody1)
    }

    suspend fun login(account: String, password: String): BaseData<LoginBean> {
        val json = JsonObject()
        json.addProperty("account", account)
        json.addProperty("password", password)
        val requestBody1 = RequestBody.create("text/plain".toMediaTypeOrNull(), json.toString())
        return requestApi.login(requestBody1)
    }
}