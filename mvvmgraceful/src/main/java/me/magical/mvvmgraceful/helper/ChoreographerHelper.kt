package me.magical.mvvmgraceful.helper

import android.view.Choreographer
import me.magical.mvvmgraceful.ext.GLog

/**
 * 掉帧检测辅助类，
 */
object ChoreographerHelper {

    var lastFrameTimeNanos:Long=0L

    fun start() {
        Choreographer.getInstance().postFrameCallback (object :Choreographer.FrameCallback{
            override fun doFrame(frameTimeNanos: Long) {
                if(lastFrameTimeNanos==0L){
                    lastFrameTimeNanos=frameTimeNanos
                    Choreographer.getInstance().postFrameCallback(this)
                    return
                }
                val diff=(frameTimeNanos- lastFrameTimeNanos)/1_000_000L
                if (diff>16.6){
                    //日志读和帧计算等需要消耗一定时间所以减掉5帧左右时间消耗
                    val droppedCount = (diff / 16.6).toInt()-5

                    if (droppedCount>2){
                        GLog.w("Monitor","绘制超时，当前绘制时间为：$diff ms,跳帧数为：$droppedCount 帧")
                    }
                }
                lastFrameTimeNanos=frameTimeNanos
                Choreographer.getInstance().postFrameCallback(this)
            }
        })
    }
}