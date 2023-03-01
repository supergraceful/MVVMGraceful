package me.magical.graceful

import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity:AppCompatActivity() {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus){
            //真正与用户有焦点的回调
        }
    }
}