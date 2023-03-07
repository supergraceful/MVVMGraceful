package me.magical.graceful.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.magical.graceful.R
import me.magical.graceful.databinding.ItemHoneImageBinding
import me.magical.graceful.databinding.ItemWallpagerBinding
import me.magical.graceful.request.bean.TypeImage
import me.magical.graceful.request.bean.VerticalBean

class WallPagerAdapter:RecyclerView.Adapter<WallPagerAdapter.Holder>(){

    private val TAG = this.javaClass.name

    private var dataList: ArrayList<VerticalBean> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<ItemWallpagerBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_wallpager,
            parent,
            false
        )

        return Holder(binding)
    }

    override fun getItemCount(): Int =dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.wallPagerImage=dataList[position]
        holder.binding.executePendingBindings()
    }

    fun addData(datas: List<VerticalBean>) {
        dataList.addAll(datas)
        notifyItemInserted(itemCount)
    }

    class Holder(val binding: ItemWallpagerBinding):RecyclerView.ViewHolder(binding.root)

}