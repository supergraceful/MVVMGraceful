package me.magical.graceful.mvvm.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView

class CustomImageView :ShapeableImageView{

    constructor(context: Context,attr:AttributeSet):super(context,attr)

    companion object{
        @JvmStatic
        @BindingAdapter(value = ["setImageUri"], requireAll = false)
        fun setImageUri(imageView: ImageView, url: String?) {
            if (!TextUtils.isEmpty(url)) {
                //使用Glide框架加载图片
                Glide.with(imageView.context)
                    .load(url)
                    .into(imageView)
            }
        }
    }

}