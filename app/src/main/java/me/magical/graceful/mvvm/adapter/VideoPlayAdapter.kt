package me.magical.graceful.mvvm.adapter

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.magical.graceful.R
import me.magical.graceful.databinding.ItemVideoBinding
import me.magical.graceful.request.bean.Parameter
import javax.inject.Inject


class VideoPlayAdapter @Inject constructor() :
    RecyclerView.Adapter<VideoPlayAdapter.Holder>() {

    private val TAG = this.javaClass.name

    private var dataList: ArrayList<Parameter> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<ItemVideoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_video,
            parent,
            false
        )

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.videoView.setVideoPath(dataList[position].playurl ?: dataList[position].playUrl)
        holder.title.text = "标题：${dataList[position].title}"
        holder.alias.text = "作者：${dataList[position].alias}"
        holder.controller.imageAlpha = 40

        holder.videoView.setOnCompletionListener {
            Log.i(TAG, "触发了")
        }

        holder.videoView.setOnInfoListener { mediaPlayer, type, i2 ->
            when (type) {
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                    //"正在缓冲"
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                    //"缓冲完成"
                }
            }
            false
        }
        var downTime: Long = 0
        holder.videoView.setOnTouchListener { _, motion ->

            if (motion.action == MotionEvent.ACTION_DOWN) {
                if (holder.videoView.isPlaying) {
                    holder.controller.visibility = View.VISIBLE
                    holder.videoView.pause()
                } else {
                    holder.controller.visibility = View.GONE
                    holder.videoView.start()
                }
                true
            }

            false
        }
    }

    override fun onViewAttachedToWindow(holder: Holder) {
        super.onViewAttachedToWindow(holder)
        holder.state = 1
        holder.videoView.start()
    }

    override fun onViewDetachedFromWindow(holder: Holder) {
        super.onViewDetachedFromWindow(holder)
        holder.state = -1
        holder.videoView.suspend()
    }

    fun addData(datas: List<Parameter>) {
        Handler(Looper.getMainLooper()).post {
            dataList.addAll(datas)
            notifyItemInserted(itemCount-1)
        }
    }

//    notifyItemRemoved

    fun reset() {
        notifyItemRangeRemoved(0,dataList.size)
        dataList.clear()
    }

    inner class Holder(binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        var videoView: VideoView
        var title: TextView
        var alias: TextView
        var controller: ImageView

        //当前holder状态，1为在前台，-1为在后台呢
        var state: Int = -1

        init {
            videoView = binding.vvItemVideoShow
            title = binding.tvItemVideoTitle
            alias = binding.tvItemVideoAlias
            controller = binding.ivItemVideoController
        }
    }
}