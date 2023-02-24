package me.magical.mvvmgraceful.helper

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import me.magical.mvvmgraceful.R
import me.magical.mvvmgraceful.base.activity.BaseAC
import me.magical.mvvmgraceful.base.application.AppManager
import me.magical.mvvmgraceful.base.fragment.BaseFM

class BaseACHelper {
}

/**
 * 判断当前点就位置是否位于输入框内
 */
fun BaseAC<*>.isShouldHideInput(v: View?, ev: MotionEvent): Boolean {
    return if (v is EditText) {
        val l = intArrayOf(1, 1)
        v.getLocationInWindow(l)
        val left = l[0]
        val top = l[1]
        val right = left + v.width
        val bottom = top + v.height
        (ev.rawX <= left) || (ev.rawX >= right) || (ev.rawY <= top) || (ev.rawY >= bottom)
    } else {
        false
    }
}

/**
 * 隐藏键盘
 */
fun BaseAC<*>.hideSoftInput(v: View?) {
    if (this.currentFocus != null && v?.windowToken != null) {
        val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        v?.clearFocus()
    }
}
