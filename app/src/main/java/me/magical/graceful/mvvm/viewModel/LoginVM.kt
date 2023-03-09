package me.magical.graceful.mvvm.viewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import me.magical.graceful.KVCode.LOGINTOKEN
import me.magical.graceful.mvvm.model.RegisterModel
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.kv.KVUtil
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.utils.ToastUtils
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var mail : UnFlowLiveData<String>

    @Inject
    lateinit var  password : UnFlowLiveData<String>

    @Inject
    lateinit var  login : UnFlowLiveData<Boolean>

    @Inject
    lateinit var  registerModel:RegisterModel

    fun login() {
        mail.value ?: let {
            ToastUtils.showLong("请输入邮箱")
            return
        }
        password.value ?: let {
            ToastUtils.showLong("请输入密码")
            return
        }

        uiRequest({
            registerModel.login(mail.value!!, password.value!!)
        }, {
            ToastUtils.showLong("登陆成功！！！")
            GLog.i("登录参数：\n $it")
            KVUtil.put(LOGINTOKEN, it!!.token)
            login.value = true
        }, {
            it.printStackTrace()
            ToastUtils.showLong("登陆失败：${it.msg}")
        }, {
            GLog.i("登陆结束")
        })
    }
}