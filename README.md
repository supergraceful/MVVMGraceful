# MVVMGraceful

[![](https://jitpack.io/v/supergraceful/MVVMGraceful.svg)](https://jitpack.io/#supergraceful/MVVMGraceful)

## 前言

目前android的原生项目开发一般都是基于某种框架进行开发，android框架由开始的`MVC`,`MVP`,到现在的`MVVM`，以及近几年较为火热的`MVI`，一步步的演化过程中各个模块的耦合度也显著降低。无论是那种框架其目的都是为了降低模块之间的耦合使得降低维护成本。这种种开发框架不仅仅局限于android开发，例如比较火热的`vue`其实也是`MVVM` 架构，其实很多在开发框架都是由前端演化出来

## 框架比较

### mvc

### mvp

### mvvm

### mvi

## 简介

使用`kotlin`+`java`混编，使用`okhttp`+`Retrofit`+协程的方式实现网络访问

## 特点



## 使用

### 1、集成

在根目录的`build.gradle`中加入

```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
```

在主项目的`app` 的`build.gradle`中添加依赖，并开启 `dataBinding`

```groovy
android {
    ...
	dataBinding {
        enabled = true
    }
}
dependencies {
     ...
	 implementation 'com.github.supergraceful:MVVMGraceful:1.0.0'
}
```

*项目中已添加的依赖*

```groovy
'androidx.appcompat:appcompat:1.3.0'
'androidx.constraintlayout:constraintlayout:2.1.0'
'androidx.core:core-ktx:1.6.0'
'androidx.multidex:multidex:2.0.1'
'com.google.android.material:material:1.2.0'

//mvvm生命周期管理
"androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0"
"androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
"androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
'androidx.lifecycle:lifecycle-extensions:2.2.0'
"androidx.activity:activity-ktx:1.2.2"
"androidx.fragment:fragment-ktx:1.3.3"
    
//kotlin
"org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.3"
"org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.32"
"org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.32"
    
"com.squareup.retrofit2:retrofit:2.9.0  "  
"com.squareup.retrofit2:converter-gson:2.9.0"
"com.squareup.retrofit2:converter-scalars:2.9.0"
"com.squareup.okhttp3:okhttp:4.9.0"
"com.github.bumptech.glide:glide:4.13.2"
"com.tencent:mmkv:1.2.13"
```

### 2、初始化

#### 2.1初始化 BaseApplication

```kotlin
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        //全局日志开关，默认开启，需要使用日志时请使用GLog方便管理
        GLog.isShow=true
    }
    
    //创建Activity管理栈，
     override fun initAppManager() {
        super.initAppManager()
    }
	
    /**
     * 初始化全局kv存储类型,默认SharedPreferences存储，已实现mmkv和SharedPreferences存储，如果需	 *	要其他的kv存储可实现KVStorage并重写getKV()
     */
    override fun getKV(): KVStorage<*> {
        return super.getKV()
        // return MMKVStorage(this)
    }
    
}
```



#### 2.2 初始网络访问

```kotlin
class MyRequest :BaseRequest() {

    val url:String=""
    lateinit var retrofit:Retrofit
    init {
        retrofit=createRetrofit(url)
    }
    /**
     * 调用Retrofit的create方法获取代理实例
     * @param service 需要代理的 api接口类
     * @param url 请求地址
     * @return 代理接口的对象
     */
    override fun <T> create(service: Class<T>, url: String): T? {
        return super.create(service, url)
    }

    /**
     * 另一种获取代理实例的方式
     */
    fun <T> create(service: Class<T>): T? {
        return retrofit.create(service)
    }
    /**
     * 初始化okhttp，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     * builder为默认实现，可以继续builder添加配置，也可以重新定义一个 OkHttpClient.Builder配置相关参数并返回
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder? {
        return null
    }

    /**
     * 初始化Retrofit，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，Protocol
     * builder为默认实现，可以继续builder添加配置，也可以重新定义一个Retrofit.Builder配置相关参数并返回
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder? {
        return null
    }

    /**
     * 添加统一的请求头,
     * 当setHttpClientBuilder重写并使用的是默认的builder的配置时可以使用该方法添加统一请求头，
     * 如果setHttpClientBuilder重写并重新定义了OkHttpClient.Builder改方法失效
     */
    override fun setHeader(): Map<String, String>? {
        return null
    }
}
```

`OkHttpClient.Builder`的默认实现

```kotlin
OkHttpClient.Builder().apply {
    //设置缓存配置 缓存最大10M
    cache(Cache(File(UtilsBridge.getApplication().cacheDir, "cxk_cache"), 10 * 1024 * 1024))
    //添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
    addInterceptor(BaseInterceptor(setHeader()))
    //添加缓存拦截器 可传入缓存天数，不传默认7天
    addInterceptor(CacheInterceptor())
    //日志拦截器
    addInterceptor(
        LoggingInterceptor.Builder().run {
            loggable(GLog.isShow)    //是否开启打印
            setLevel(Level.BASIC)          //打印的等级
            log(Platform.INFO)             //打印日志类型
            request("Request")             // request的Tag
            response("Response")           // Response的Tag
            addHeader(
                "log-header",
                "I am the log request header."
            ) // 添加打印头, 注意 key 和 value 都不能是中文
            build()
        }
    )

    //超时时间 连接、读、写
    connectTimeout(10, TimeUnit.SECONDS)
    readTimeout(5, TimeUnit.SECONDS)
    writeTimeout(5, TimeUnit.SECONDS)
    //设置线程池
    connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
}
```

`Retrofit.Builder`默认实现

```kotlin
Retrofit.Builder()
    .client(mOkHttpClient)
```



### 3、使用

#### 3.1 Activity创建

##### 3.1.1 创建布局文件

布局标签最顶层使用layout包裹，此时编译器会根据当前`xml`生成对应的映射文件,文件名称会根据xml文件名称命名，名称规则为`xml文件名+Binding`，例如:`xml`文件名称为`test_activity.xml`,生成的映射文件名称为`TestActivityBinding`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable
            name="newViewModel"
            type="me.magical.graceful.news.NewsViewModel" />
    </data>
    <androidx.constraintlayout.widget.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="match_parent"
        android:layout_height="match_parent">

        <Button
            android:text="不带参触发"
            android:id="@+id/bt_test_news"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{newsData.getNews}"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>
        <Button
            android:text="不带参触发"
            android:id="@+id/bt_test_news_param"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{()-newsData.getNews(1,5)}"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/bt_test_news"/>
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{newsData.resultData.toString()}"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/bt_test_news_param"/>
    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>

```



##### 3.1.2 创建无viewModel的activity

```kotlin
class TestActivity:BaseActivity<TestActivityBinding>() {

    /**
     * 绑定的xml布局id
     */
    override fun getLayout()=R.layout.test_activity

    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {

        /**
         * https://github.com/ybq/Android-SpinKit
         * 设置默认loading样式
         * 如果使用默认的loading，可以调用setDefaultLoading设置样式，非必须调用
         * @param spriteContainer loading样式，默认Wave()
         * 以下为支持的样式
         * RotatingPlane()，DoubleBounce()，Wave()，WanderingCubes()，Pulse()，			
         * ThreeBounce()，Circle()，CubeGrid()，FadingCircle()，FoldingCube()，			
         * ChasingDots(),RotatingCircle()
         * MultiplePulse()，PulseRing()，MultiplePulseRing()
         *
         * @param color loading的颜色，默认 #03DAC5
         */
        // setDefaultLoading(spriteContainer(), color)
    }


    /**
     * 如果使用自定义的loading需要重写以下两个方法
     */
    override fun showLoading(title: String) {
        super.showLoading(title)
    }

    override fun dismissLoading() {
        super.dismissLoading()
    }

}
```



##### 3.1.3 创建绑定viewModel的Activity

继承`BaseMVVMActivity`(**`BaseMVVMActivity`继承自`BaseActivity`**) 添加对应的`binding`和`viewMode`，并重写相应方法。会根据泛型传入的类生成相应的对象：

- 第一个泛型参数：为xml界面映射生成的文件，可在当前`activity`下直接以`mBinding.元素id`的方式获取元素实例，无需调用`findViewById`的方式获取元素实例；

- 第二个泛型参数：为自定义的继承自`BaseViewModel`的`viewmodel`的类，可在当前`activity`下以`mViewModel`的方式调用自定义的`viewmodel`类的参数及方法

```kotlin
class TestActivity:BaseMVVMActivity<TestActivityBinding,TestViewModel>() {

    /**
     * 创建viewmodel的观察者
     */
    override fun createObserver() {
        
    }

    /**
     * 返回VariableId不建议使用BR._all，尽量使用xml中使data定义的name所生成的BR值
     */
    override fun initVariableId(): Int {
        return BR.newViewModel
    }

    /**
     * 绑定的xml布局id
     */
    override fun getLayout(): Int {
       return R.layout.test_activity
    }
//    override fun getLayout()=R.layout.test_activity
    
    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {
        
    }
}
```



#### 3.2 创建fragment

##### 3.2.1 无viewModel的fragment

```kotlin
class TestFragment : BaseFragment<TestFragmentBinding>() {

    /**
     * 懒加载
     */
    override fun lazyLoadData() {

    }

    /**
     * fragment所要绑定的xml的id
     */
    override fun getLayout(saedInstanceState: Bundle?) = R.layout.test_fragment

    /**
     * 初始化一些ui相关的东西
     */
    override fun initView(savedInstanceState: Bundle?) {
        
    }
        /**
     * 懒加载延迟时间毫秒，默认300毫秒
     */
    override fun lazyLoadTime(): Long {
        return super.lazyLoadTime()
    }
}
```

##### 3.2.2 创建绑定viewModel的fragment

`BaseMVVMFragment`   继承自`BaseFragment`

```kotlin
class TestMVVMFragment :BaseMVVMFragment<TestFragmentBinding,TestMVVMVViewModel>() {

    /**
     * 返回VariableId不建议使用BR._all，尽量使用xml中使data定义的name所生成的BR值
     */
    override fun initVariableId() =BR._all
    /**
     * 绑定的xml布局id
     */

    override fun getLayout(savedInstanceState: Bundle?)= R.layout.test_fragment
    /**
     * 懒加载
     */
    override fun lazyLoadData() {

    }

    /**
     * 创建一些mBinding的观察者
     */
    override fun createObserver() {

    }

    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {

    }

    /**
     * 懒加载延迟时间毫秒，默认300毫秒
     */
    override fun lazyLoadTime(): Long {
        return super.lazyLoadTime()
    }
}
```

#### 3.3 创建viewmodel

```kotlin
class TestViewModel:BaseViewModel() {

    /**
     * 建议使用UnFlowLiveData实现livedata功能，防止数据倒灌
     *
     * 数据倒灌可以看下这个博客 https://xiaozhuanlan.com/topic/6719328450
     */
    val resultData=UnFlowLiveData<String>()


    fun getNews(v: View) {
        uiRequest({
            model.getNews("1", "5")
        })

    }

    fun getNews(page: Int,count:Int) {
        
        uiRequest(requestData,{
            model.getNews(page.toString(), count.toString())
        })

    }

}
```

`BaseViewModel`定义了以下以`livedata`的`ui`监听方法，可直接在`BaseViewModel`中调用

```kotlin
// 弹出Toast方法
showToastEvent（内容）
// 弹出Loading，最终调用的是BaseActivity中showLoading
showLoading(标题)
// 隐藏Loading，最终调用的是BaseActivity中dismissLoading
dismissLoading()
// 跳转,从当前所绑定界面跳转
startActivity(跳转类，携带的参数Bundle)
// finish，finsh当前界面
finish()
// 返回，关闭当前界面
onBackPressed()
```



#### 3.4 model以及网络访问

##### 3.4.1 api创建

因为网络请求是耗时的操作，本框架利用的是协程创建的请求，所以在请求的具体接口要加`suspend`关键字进行挂起操作，在`retrofit`的高版本中可以直接获取响应结果

```kotlin
interface RequestApi {

    @FormUrlEncoded
    @POST("getWangYiNews")
    suspend fun getNews(
        @Field("page") page: String?,
        @Field("count") count: String?
    ): BaseBean<List<DtoBean>>?

}
```

##### 3.4.2 model创建

利用`retrofit`的`create`方法获取`RequestApi`的实例对象，进行访问，因为调用的方法是挂起(`suspend`)函数，同样的创建的方法也需要添加`suspend`关键字（*挂起的函数方法只能在协程中或另一个挂起方法中调用*）

```kotlin
class NewsModel {

    suspend fun getNews(page:String,count:String): BaseBean<List<DtoBean>>? {
        return MyRequest.instance.create(RequestApi::class.java)?.getNews(page,count)
    }
}
```

#### 3.5 发起请求并处理结果（需要在BaseViewModel，中调用以下方法）

##### 3.5.1函数回调请求方式

###### 3.5.1.1 默认ui

```kotlin
/**
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
}, {
    //成功回调，it为请求成功的结果
    resultData.postValue(it)
}, {
    /**
     * 请求成功的回调，it为CustomException对象保存着请求失败的信息，
     * it.code：失败code码，包括自定code以及服务器返回失败的code
     * it.msg：失败的简略信息
     * it.message：失败的详细堆栈信息
     */
    Log.e("getNews: ", "${it.msg}: ${it.message}")
}, {
    //完成时回调
}, true, true, "加载中")


```

###### 3.5.1.2 无ui

```kotlin


/**
 * 高阶函数回调请求方式,无ui
 * 参数一：请求方法，需要返回BaseBean<T>类型的结果，不可为空
 * 参数二：开始时回调，可为空
 * 参数二：成功回调，请求成功的结果，可为空
 * 参数三：失败回调，返回CustomException，可为空
 * 参数四：完成时回调，无返回，可为空
 */
request({
    model.getNews("1", "5")
}, {
    //请求开始时的回调
}, {
    //请求成功的回调，it为请求成功的结果
}, {
    /**
     * 请求成功的回调，it为CustomException对象保存着请求失败的信息，
     * it.code：失败code码，包括自定code以及服务器返回失败的code
     * it.msg：失败的简略信息
     * it.message：失败的详细堆栈信息
     */

}, {
    //请求完成的回调
})
```

###### 3.5.1.3 函数回调转接口回调

##### 3.5.2 多状态函数返回值方式

###### 3.5.2.1 默认ui

`DataState`为获取的状态可注册观察者进行状态（请求前，请求成功，请求失败，请求完成）观察

```kotlin
/**
 * 多状态函数返回值方式，带ui
 * 参数一：UnFlowLiveData<DataState<T>>() T为请求成功返回的bean，不可为空
 * 参数二：请求方法，需要返回BaseBean<T>类型的结果，不可为空
 * 参数三：是否弹加载出loading，默认为true，可为空，选择形参数，调用BaseViewModel.showLoading
 * 参数四：是否弹失败提示toast，默认为true，可为空，弹出的是失败回调的简略信息，选择形参数，调用BaseViewModel.showToast
 * 参数五：loading的标题，默认为 "加载中...",可为空，选择形参数，
 */

val requestData=UnFlowLiveData<DataState<List<DtoBean>>>()

uiRequest(
    requestData,
    {
        model.getNews(page.toString(), count.toString())
    }, true, true, "加载中"
)
```

在activity或者在fragment中监听参数的获取

```kotlin
 mViewModel.requestData.observe(this){
            when(it){
                is DataState.OnStart->{
                    //请求开始时回调，it.message是返回loading标题内容
                    it.message
                }
                is DataState.OnSuccess->{
                    //成功时的回调，it.data是请求成功的结果
                    it.data
                }
                is DataState.OnError->{
                    //失败的回调，it.exception是请求失败返回的CustomException
                    it.exception
                }
                is DataState.OnComplete->{
                    //请求完成时的回调
                }
            }
        }
```

###### 3.5.2.2 无ui



```kotlin
/**
 *  多状态函数返回值方式,无ui
 *  参数一：UnFlowLiveData<DataState<T>>() T为请求成功返回的bean，不可为空
 *  参数二：请求方法，需要返回BaseBean<T>类型的结果，不可为空
 */
request(requestData) {
    model.getNews(page.toString(), count.toString())
}
```

监听方式同***默认ui***方式



###### 3.5.2.3 状态函数转换为回调式

当用户想将状态函数转换为回调的方式时可调用`parse`方式回调

```kotlin
/**
 * 参数一：DataState对象，非空
 * 参数二：请求开始时回调，可为空
 * 参数三：请求成功回调，非空
 * 参数三：请求失败回调，可为空
 * 参数三：请求完成回调，可为空
 */
parse(
    requestData.value!!, {
        //请求开始时参数 it string类型
    }, {
        //请求成功数据 it 具体类型需要根据请求返回参数
    }, {
        //请求失败数据 it CustomException类型
    }, {

    })
```

##### 3.5.3 CustomException

失败的回调的封装，继承自`Throwable`

**作用：**将请求数据过程中抛出的异常，以及非成功请求的数据（访问成功但是返回的不是成功的数据），将异常信息整理存储的类

**参数：**

- `code`：错误码，当为`try catch`捕获的异常时使用的是框架默认的值，当为服务器返回的错误时code值和返回的code一致
- `msg`：异常的简略信息,当为`try catch`捕获的异常时信息是框架自定义的简略信息（可通过message获取堆栈异常信息），，当为服务器返回的错误时msg是服务器返回的错误信息

[]: 

#### 3.6 文件下载

##### 3.6.1 DownLoadManager （均为静态方法）

- **downLoad 下载**

  | 参数 | 类型 | 是否可为空 | 说明 |
  | ------- | -------- | ---------- | ---- |
  | `tag`  | `String` | 否 | 下载标志 |
  | `url`   | `String` | 否 | 下载地址 |
  | `headers` | `HashMap` | 是 | 请求头 |
  | `savePath` | `String` | 否 | 保存本地的地址，最后以为不能为 `/` |
  | `saveName` | `String` | 否 | 保存本地的文件名称 |
  | `listener` | `OnDownLoadListener` | 否 | 回调接口 |
  | `again` | `Boolean` | 是 | 是否重新下载，当下载任务存在任务时是否重新下载，默认false |




- **cancel 取消下载**

  tag：下载标志，String类型

  

- **pause 暂停**

  tag：下载标志，String类型

  

- **doDownLoadCancelAll  取消所有**

  

- **doDownLoadPauseAll  暂停所有**



##### 3.6.2 FileTool（均为静态方法）

- **getBasePath 获取App文件的根路径** 

  

- **bytes2kb  将字节长度转换并添加单位**

  bytes：字节长度，Long类型

##### 3.6.3 OnDownLoadListener 下载回调

```kotlin
interface OnDownLoadListener {

    //下载成功
    fun onDownloadSuccess(tag: String, path: String, size: Long)

    //下载错误
    fun onDownloadError(tag: String, throwable: Throwable)

    //等待下载
    fun onDownloadPrepare(tag: String)

    //下载暂停
    fun onDownLoadPause(tag: String)

    /**
     * 下载进度
     * @param key url
     * @param progress  进度
     * @param read  读取（实际的文件位置）
     * @param count 总共长度
     * @param done  是否完成
     */
    fun onDownloadProgress( tag: String,progress: Int, read: Long,count: Long,done: Boolean)
}
```



##### 3.6.4 回调转换为状态函数

- `getStateListener`传入 `UnFlowLiveData<DownloadState>()`类型的参数，并返回创建的回调，参数为livedata类型，可以通过observer观察得到文件下载的信息



***下载实例***

```kotlin

/*
*
* viewModelScope是一个绑定到你的ViewModel的CoroutineScope。这意味着当ViewModel清除了该作用域中的协
* 程时，该作用域中的协程也会被取消。
*/
val listener = UnFlowLiveData<DownloadState>()

viewModelScope.launch(Dispatchers.IO) {
    DownLoadManager.downLoad(
        tag,
        "https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip",
        FileTool.getBasePath(),
        "test.zip",
        getStateListener(listener)
    )
}
```

***转换实例***


```kotlin
mViewModel.listener.observe(this){
    when(it){
        /**
         * 下载过程
         * progress：下载进度百分数, Int类型
         * read：已经下载的文件大小byte数, Long类型
         * count：文件总大小, Long类型
         * done：是否下载完成，Boolean类型
         */
        is DownloadState.Progress->{
            val progress= it.progress
            val readSize= FileTool.bytes2kb(it.read)
            val totalSize= FileTool.bytes2kb(it.count)

            mBinding.tvProgress.text="下载进度----->$progress %，   ----->$readSize/$totalSize"
            if (it.done){
                mBinding.tvProgress.text="已完成"
            }

        }
        /**
         * 下载成功
         * path：保存地址的完整路径
         * size：文件大小
         */
        is DownloadState.Success -> {
            GLog.e("createObserver: ","保存地址${it.path} ,文件大小${it.size} ")
        }
        /**
         * throwable:异常的throwable
         */
        is DownloadState.Error -> {
            mBinding.tvProgress.text = "下载错误-->${it.throwable.message}"
            GLog.e("createObserver: ", "下载错误-->${it.throwable.message}")
        }
        /**
         * 等待下载
         */
        is DownloadState.Prepare->{}

        /**
         * 暂停下载
         */
        is DownloadState.Pause->{}
    }
}
```

## 鸣谢

[ 借鉴了很多鸡哥框架 ：JetpackMvvm ](https://github.com/hegaojian/JetpackMvvm)

