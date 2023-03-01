package me.magical.graceful.mvvm.viewModel

import me.magical.graceful.KVCode.LOGINTOKEN
import me.magical.graceful.mvvm.model.RegisterModel
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.kv.KVUtil
import me.magical.mvvmgraceful.helper.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.utils.ToastUtils

class LoginVM : BaseViewModel() {

    val mail = UnFlowLiveData<String>()

    val password = UnFlowLiveData<String>()

    val login = UnFlowLiveData<Boolean>()

    val registerModel = RegisterModel()

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
            registerModel.login(mail.value!!,password.value!!)
        }, {
            ToastUtils.showLong("登陆成功！！！")
            GLog.i("登录参数：\n $it")
            KVUtil.put(LOGINTOKEN,it!!.token)
            login.value=true
        }, {
            it.printStackTrace()
            ToastUtils.showLong("登陆失败：${it.msg}")
        }, {
            GLog.i("登陆结束")
        })
    }
}