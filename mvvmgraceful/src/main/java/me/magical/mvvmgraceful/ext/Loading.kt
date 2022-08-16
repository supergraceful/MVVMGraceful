package me.magical.mvvmgraceful.ext

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil

import me.magical.mvvmgraceful.R
import me.magical.mvvmgraceful.databinding.LoadingItemBinding
import me.magical.mvvmgraceful.ui.loading.sprite.SpriteContainer
import me.magical.mvvmgraceful.ui.loading.style.Wave

class Loading(context: Context, spriteContainer: SpriteContainer, color:Int=R.color.colorAccent) {

    var mLoading: Dialog = Dialog(context, R.style.Theme_audioDialog)

    val contentBing: LoadingItemBinding?

    var title: String = ""
        set(value) {
            contentBing?.tvLoadingTitle?.text = value
            field=value
        }

    constructor(context: Context) : this(context, Wave())

    init {
        contentBing = DataBindingUtil.bind<LoadingItemBinding>(
            LayoutInflater.from(context).inflate(R.layout.loading_item, null)
        )
        contentBing!!.spinKit.setIndeterminateDrawable(spriteContainer)
        contentBing.spinKit.setColor(color)
        mLoading.setContentView(contentBing.root)
        mLoading.setCanceledOnTouchOutside(false)
    }


    fun show() {
        if (mLoading.isShowing) {
            mLoading.dismiss()
        }
        mLoading.show()
    }

    fun dismiss() {
        if (mLoading.isShowing) {
            mLoading.dismiss()
        }
    }
}