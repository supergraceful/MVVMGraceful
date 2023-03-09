package me.magical.graceful.mvvm.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.magical.graceful.R
import me.magical.graceful.databinding.ItemHoneImageBinding
import me.magical.graceful.databinding.ItemVideoBinding
import me.magical.graceful.mvvm.activity.PictureViewActivity
import me.magical.graceful.request.bean.Parameter
import me.magical.graceful.request.bean.TypeImage
import me.magical.graceful.request.bean.TypeImageBean
import me.magical.mvvmgraceful.ext.GLog
import javax.inject.Inject

class HomeImageAdapter @Inject constructor() : RecyclerView.Adapter<HomeImageAdapter.Holder>() {

    private val TAG = this.javaClass.name

    private var dataList: ArrayList<TypeImage> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeImageAdapter.Holder {
        val binding = DataBindingUtil.inflate<ItemHoneImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_hone_image,
            parent,
            false
        )

        return Holder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.typeImage = dataList[position]
        holder.binding.onClick=ClickBinding()
        holder.binding.executePendingBindings()
    }

    fun addData(datas: List<TypeImage>) {
        dataList.addAll(datas)
        notifyItemInserted(itemCount)
    }

    inner class Holder(val binding: ItemHoneImageBinding) : RecyclerView.ViewHolder(binding.root)

    inner class ClickBinding() {

        public fun itemClick(view: View,typeImage: TypeImage) {
//            val intent = Intent(view.context, PictureViewActivity::class.java)
//            intent.putExtra("img", typeImage.url)
//            view.context.startActivity(intent)
            GLog.i(typeImage.toString())
        }
    }
}