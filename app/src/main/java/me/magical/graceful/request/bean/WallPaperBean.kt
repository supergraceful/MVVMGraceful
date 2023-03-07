package me.magical.graceful.request.bean

import me.magical.mvvmgraceful.request.core.BaseBean
import me.magical.mvvmgraceful.utils.NetworkUtil.url

data class WallPaperBean(val msg: String, val res: ResBean, val code: Int) : BaseBean<List<VerticalBean>>() {
    override fun isSuccess(): Boolean {
        return code == 0
    }

    override fun getResponseCode(): Int {
        return code
    }

    override fun getResponseData(): List<VerticalBean>? {
        return res.vertical
    }

    override fun getThrowableMessage(): String? {
        return msg
    }
}


data class ResBean(val vertical: List<VerticalBean>)

data class VerticalBean(
    val preview: String,
    val thumb: String,
    val img: String,
    val views: Int,
    val rule: String,
    val ncos: Int,
    val rank: Int,
    val source_type: String,
    val wp: String,
    val xr: Boolean,
    val cr: Boolean,
    val favs: Int,
    val atime: Double,
    val id: String,
    val store: String,
    val desc: String,
    val cid: List<String>,
    val tag: List<String>,
    val urlList: List<String>
)