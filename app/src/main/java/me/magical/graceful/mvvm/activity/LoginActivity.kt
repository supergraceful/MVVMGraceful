package me.magical.graceful.mvvm.activity

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import me.magical.graceful.BR
import me.magical.graceful.KVCode.LOGINTOKEN
import me.magical.graceful.R
import me.magical.graceful.databinding.ActivityLoginBinding
import me.magical.graceful.mvvm.viewModel.LoginVM
import me.magical.mvvmgraceful.base.activity.BaseMVVMAC
import me.magical.mvvmgraceful.ext.kv.KVUtil

@AndroidEntryPoint
class LoginActivity : BaseMVVMAC<ActivityLoginBinding, LoginVM>() {


    override fun initVariableId(): Int =BR.loginVM

    override fun getLayout(): Int = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.tvLoginRegister.setOnClickListener {
            startActivity(RegisterActivity::class.java)
        }
        loginSuccess()
    }
    override fun createObserver() {
        mViewModel.login.observe(this){
            loginSuccess()
        }
    }

    private fun loginSuccess(){
        val token=KVUtil.getValue(LOGINTOKEN,"")
        if (!token.isNullOrEmpty()){
            startActivity(MainActivity::class.java)
        }
    }
}