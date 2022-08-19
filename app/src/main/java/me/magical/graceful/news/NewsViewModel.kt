package me.magical.graceful.news

import android.view.View
import me.magical.graceful.request.DtoBean
import me.magical.mvvmgraceful.base.BaseViewModel
import me.magical.mvvmgraceful.ext.GLog
import me.magical.mvvmgraceful.ext.uiRequest
import me.magical.mvvmgraceful.livedata.UnFlowLiveData
import me.magical.mvvmgraceful.request.core.DataState

class NewsViewModel : BaseViewModel() {

    /**
     * 建议使用UnFlowLiveData实现livedata功能，防止数据倒灌
     *
     * 数据倒灌可以看下这个博客 https://xiaozhuanlan.com/topic/6719328450
     */
    val resultData = UnFlowLiveData<DtoBean>()

    val requestData = UnFlowLiveData<DataState<DtoBean>>()

    val model = NewsModel()


    fun getNews(v: View) {
        /**
         * 高阶函数回调请求方式,带ui
         * 参数一：请求方法，需要返回BaseBean<T>类型的结果，不可为空
         * 参数二：成功回调，请求成功的结果，可为空
         * 参数三：失败回调，返回CustomException，可为空
         * 参数四：完成时回调，无返回，可为空
         * 参数五：是否弹加载出loading，默认为true，可为空，选择形参数，调用BaseViewModel.showLoading
         * 参数六：是否弹失败提示toast，默认为true，可为空，弹出的是失败回调的简略信息，选择形参数，调用BaseViewModel.showToast
         * 参数七：loading的标题，默认为 "加载中...",可为空，选择形参数，
         */
        uiRequest({
            model.getNews("1", "5")
        },::success, {
            GLog.e(null, "${it.msg}: ${it.message}")
        }, {
            //完成时回调
        }, true, true, "加载中...")


//        /**
//         *
//         * 无ui样式
//         */
//        uiRequest({
//            model.getNews("1", "5")
//        }, {
//            //成功回调，it为请求成功的结果
//            resultData.postValue(it)
//            GLog.i(null,it.toString())
//        }, {
//            GLog.e(null, "${it.msg}: ${it.message}")
//        }, {
//            //完成时回调
//        })
    }

    fun getNews(page: Int, count: Int) {
        /**
         * 多状态函数返回值方式，带ui
         *  参数一：UnFlowLiveData<DataState<T>>() T为请求成功返回的bean，不可为空
         *  参数二：请求方法，需要返回BaseBean<T>类型的结果，不可为空
         * 参数三：是否弹加载出loading，默认为true，可为空，选择形参数，调用BaseViewModel.showLoading
         * 参数四：是否弹失败提示toast，默认为true，可为空，弹出的是失败回调的简略信息，选择形参数，调用BaseViewModel.showToast
         * 参数五：loading的标题，默认为 "加载中...",可为空，选择形参数，
         */
        uiRequest(requestData, {
            model.getNews(page.toString(), count.toString())
        },true,true,"加载中")


//        /**
//         * 无默ui样式的
//         */
//        request(requestData) {
//            model.getNews(page.toString(), count.toString())
//        }

    }

    /**
     * 进阶用法
     */
    fun <T>success(bean:T){
        resultData.postValue(bean as DtoBean)
        GLog.e("success11111",bean.toString())
    }
}