package me.magical.graceful.mvvm.activity

import android.os.Bundle
import android.os.CountDownTimer
import dagger.hilt.android.AndroidEntryPoint
import me.magical.graceful.BR
import me.magical.graceful.R
import me.magical.graceful.databinding.ActivityRegisterBinding
import me.magical.graceful.mvvm.viewModel.RegisterVM
import me.magical.mvvmgraceful.base.activity.BaseMVVMAC

@AndroidEntryPoint
class RegisterActivity : BaseMVVMAC<ActivityRegisterBinding, RegisterVM>() {
    private var timer: CountDownTimer? = null

    override fun initVariableId(): Int = BR.registerVM

    override fun getLayout(): Int = R.layout.activity_register

    override fun createObserver() {
        mViewModel.countdown.observe(this){
            when(it){
                0->{
                    mBinding.btRegisterCaptcha.isEnabled=false
                }
                1->{
                    countDownTimer()
                }
                -1->{
                    mBinding.btRegisterCaptcha.text="重新发送"
                    mBinding.btRegisterCaptcha.isEnabled=true
                    timer?.cancel()
                }
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    fun countDownTimer() {
        var num = 60

        timer = object : CountDownTimer((num + 1) * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                if (num>0){
                    mBinding.btRegisterCaptcha.text="$num"
                    num--
                }else{
                    timer?.cancel()
                }
            }

            override fun onFinish() {
                mBinding.btRegisterCaptcha.isEnabled=true
                mBinding.btRegisterCaptcha.text="重新发送"
            }
        }

        timer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}