package me.magical.graceful.mvvm.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.magical.graceful.KVCode
import me.magical.mvvmgraceful.ext.kv.KVUtil


class SplashActivity:AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!this.isTaskRoot){
            finish()
            return
        }
        toMain()
    }
    private fun toMain(){
        val token= KVUtil.getValue(KVCode.LOGINTOKEN,"")
        val intent = if (!token.isNullOrEmpty()){
            Intent(this, MainActivity::class.java)
        }else{
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}