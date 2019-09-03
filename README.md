### 具体使用方式请看 https://mp.csdn.net/mdeditor/100513698

#  网络请求库使用方法
### 首先在gradle中引入NetWorkLibrary项目
### 一、 MVP模块
 
1. 新建一个业务模块（fragment 或 activity, eg: LoginActivity）
2. 首先创建View层接口（eg： LoginView, 实现ABaseView）
```
    interface LoginView: ABaseView {}
```
3. 创建presenter（eg： LoginPresenter ,  实现ABasePresenter,泛型为View, 构造参数为view）
```
    class LoginPresenter(view: CodeView): ABasePresenter<LoginView>(view) {
           // 逻辑
           fun login() {} 
           ...
    }
```
4. activity 继承 BaseMvpActivity, 泛型为刚刚创建的presenter，并实现view接口
```
    interface LoginActivity: BaseMvpActivity<LoginPresenter>(), ABaseView {
        // todo 其中在这里初始化presenter
        override fun initPresenter(): CodePresenter {
                return LoginPresenter(this)
        }
        
        // 然后可以直接使用父对象的mPresenter变量, 这时mPresenter指向的是LoginPresenter
        private fun doHttpDeal() {
            mPresenter.login()
        }
    }
```


### 二、 网络请求

1. 需要在application中初始化网络请求对象，便于使用application中的一些配置,
   init()方法中有5个参数,其中参数1和参数2必填，其余可不传，这是会自动使用默认的：
   1) context
   2) baseUrl,网络请求的基础路径
   3) 是否是debug模式, 默认为false
   4) 自定义拦截器，如果不想使用默认的拦截器，可自定义拦截器，以list的形式传递进来，默认不加拦截器
   5) 自定义缓存方式，如果不想使用默认的缓存方式，可自定义一个类继承AbsCookieResult对象，默认使用ACache缓存方式
```
    class ABaseApp: BaseApplication() {
        
        override fun onCreate() {
            super.onCreate()
            NetworkHelper.init(
                applicationContext,
                ApiConstants.BASE_URL,
                true,
                mutableListOf<Interceptor>(PublicParamsInterceptor()),
                null
            )
        }
    }
```

2. 面向对象，把每一个请求封装成一个对象，首先创建一个配置对象，继承AbsRequestOptions，并重写createService()方法，
   配置项可在构造参数中进行改变， createService()中返回请求的service

```
    // 这里泛型为String，代表返回json字符，如果不想自己解析，可直接使用解析对象 
    class LoginOptions(username:Stirng, pwd:String): AbsRequestOptions<String> {
    
           init {
                // 配置baseUrl
                baseUrl = "http://t1.fsyuncai.com/"            
           }
           
           override fun createService(retrofit: Retrofit): Observable<String> {
                return retrofit.create(VipService::class.java).memberCertification(body)
           }
    }
```

3. 网络请求使用repo包下面的 HttpManager，请求时：
```
    fun login() {
        // 直接将配置作为参数传进来，配置对象中返回了Obervable对象
        HttpManager.instance.doHttpDeal(LoginOptions("username", "pwd"),
        
                // 这里泛型为String，代表返回json字符，如果不想自己解析，可直接使用解析对象 ，
                // INetworkCallback为回调类，其构造参数为LifecycleProvider, 是为了更方便的控制网络请求的生命周期
                // 所以这里的请求只能在继承了ABasePresenter, 或 BaseMvpActivity中使用
                object : INetworkCallback<String>(this) {
                
                    override fun cache(json:String):Boolean {
                        // true代表使用缓存，false代表不使用缓存， 
                        // 这里可以通过一个变量进行更灵活的控制
                        return true
                    }
                
                    override fun onSuccess(obj: String, cookieListener: CookieResultListener) {
        
                        val response = Gson().fromJson<HttpResponseResult>(obj, HttpResponseResult::class.java)
        
                        if (response.errorCode != 0) {
                            ToastUtil.showCustomToast(response.errorMessage ?: "服务器错误")
                            return
                        }
                        // 如果需要缓存,在这里自定义缓存的时机，如果需要缓存，重写onCache()方法，并返回true,缓存的内容会走到onCache回调
                        // 如果不自定义时机的话，有可能请求code码错误了也会缓存，所以在这里判断成功了才去缓存
                        cookieListener.saveCookie()
                        
                        ToastUtil.showCustomToast("升级认证会员提交成功")
                        mView.submitSuccess()
                    }
                    
                    // 请求错误回调，如果不想使用用默认的，请重写此方法，并注释掉super.onError()这一行
                    override fun onError() {
                        super.onError()
                    }
                })
    }
```

4. options配置项：
```
    baseUrl: 基础路径（域名）
    cacheTimeForNetwork: 有网缓存时间，单位是ms，默认30s
    cacheTimeForNoNetwork: 无网缓存时间，单位是ms， 默认是1天
    cacheUrl: 缓存路径（缓存时必填）
    isCache: 是否缓存
    retryCount: 超时重连次数
    retryDelay: 超时重连延时（ms）（失败后多长时间进行重连）
    retryIncreaseDelay: 超时重连叠加延时（ms）（比如第一次100毫秒后重试，第二次就是 100 + retryIncreaseDelay毫秒后重试了。。。）
    timeOut: 设置超时时间（s），默认6s，如果上传图片的话可能会超时，这时可配置大一些
```
