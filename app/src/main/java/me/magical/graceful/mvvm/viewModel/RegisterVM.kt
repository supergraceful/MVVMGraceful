package me.magical.graceful.mvvm.viewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import me.magical.graceful.KVCode.SENDVERIFICATIONCODE
import me.magical.graceful.mvvm.model.RegisterModel
import me.magical.graceful.request.bean.RegisterBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.kv.KVUtil
import me.magical.mvvmgraceful.helper.request
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.core.CustomException
import me.magical.mvvmgraceful.request.core.ResponseImpl
import me.magical.mvvmgraceful.utils.ToastUtils
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor():BaseViewModel() {

    @Inject
    lateinit var mail : UnFlowLiveData<String>
    @Inject
    lateinit var password : UnFlowLiveData<String>
    @Inject
    lateinit var captcha : UnFlowLiveData<String>
    @Inject
    lateinit var countdown : UnFlowLiveData<Int>
    @Inject
    lateinit var registerModel:RegisterModel

    fun sendMail(){
        mail.value ?: let {
            ToastUtils.showLong("请输入邮箱")
            return
        }
        request({
            GLog.i("请求参数：${mail.value!!}")
            registerModel.sendVerificationCode(mail.value!!)
        },{
            countdown.value=0
        },{
                ToastUtils.showLong("发送成功！！！！")
                countdown.value=1
        },{
            ToastUtils.showLong("验证码发送出错")
            GLog.e("错误信息：${it.msg}")
            GLog.e(it.printStackTrace())
            countdown.value=-1
        })
    }

    fun register(){
        mail.value ?: let {
            ToastUtils.showLong("请输入邮箱")
            return
        }
        password.value ?: let {
            ToastUtils.showLong("请输入密码")
            return
        }
        captcha.value ?: let {
            ToastUtils.showLong("请输入验证码")
            return
        }

        uiRequest({
            registerModel.register(mail.value!!,captcha.value!!, password.value!!)
        },object :ResponseImpl<RegisterBean>{
            override fun onStart() {
                GLog.i("请求发送")
            }

            override fun onError(exception: CustomException) {
                exception.printStackTrace()
                GLog.e("请求失败:${exception.msg}")
            }

            override fun onComplete() {
                GLog.i("请求结束")
            }
            override fun onSuccess(data: RegisterBean) {
                GLog.i("请求成功 \n ${data.toString()}")
                finish()
            }
        })
    }
}