package com.cheng.okgo.http.okgo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import com.cheng.okgo.StrUtils;
import com.cheng.okgo.http.ResultResponse;
import com.cheng.okgo.http.okgo.cache.CacheEntity;
import com.cheng.okgo.http.okgo.cache.CacheMode;
import com.cheng.okgo.http.okgo.callback.BitmapCallback;
import com.cheng.okgo.http.okgo.callback.FileCallback;
import com.cheng.okgo.http.okgo.callback.JsonCallback;
import com.cheng.okgo.http.okgo.callback.StringCallback;
import com.cheng.okgo.http.okgo.cookie.CookieJarImpl;
import com.cheng.okgo.http.okgo.https.HttpsUtils;
import com.cheng.okgo.http.okgo.interceptor.HttpLoggingInterceptor;
import com.cheng.okgo.http.okgo.model.HttpHeaders;
import com.cheng.okgo.http.okgo.model.HttpParams;
import com.cheng.okgo.http.okgo.model.Progress;
import com.cheng.okgo.http.okgo.model.Response;
import com.cheng.okgo.http.okgo.request.DeleteRequest;
import com.cheng.okgo.http.okgo.request.GetRequest;
import com.cheng.okgo.http.okgo.request.HeadRequest;
import com.cheng.okgo.http.okgo.request.OptionsRequest;
import com.cheng.okgo.http.okgo.request.PatchRequest;
import com.cheng.okgo.http.okgo.request.PostRequest;
import com.cheng.okgo.http.okgo.request.PutRequest;
import com.cheng.okgo.http.okgo.request.TraceRequest;
import com.cheng.okgo.http.okgo.request.base.Request;
import com.cheng.okgo.http.okgo.utils.HttpUtils;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * OkGo
 * 使用方法如sample()方法所示
 github源码
 https://github.com/jeasonlzy/okhttp-OkGo

 使用详解
 https://github.com/jeasonlzy/okhttp-OkGo/wiki

 前提：http协议了解
 http://www.cnblogs.com/ranyonsue/p/5984001.html
 http://www.ruanyifeng.com/blog/2016/08/http.html
 https://zhuanlan.zhihu.com/p/24913080

 Retrofit2 完全解析 探索与okhttp之间的关系
 http://blog.csdn.net/lmj623565791/article/details/51304204

 注：如正常使用上传下载直接用OkGo就行了，如果需要有多个任务管理可以使用OkDownload与OkUpload类
 */
public class OkGo {
    public static final long DEFAULT_MILLISECONDS = 60000;      //默认的超时时间
    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）

    private Application context;            //全局上下文
    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient okHttpClient;      //ok请求的客户端
    private HttpParams mCommonParams;       //全局公共请求参数
    private HttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private CacheMode mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期

    private OkGo() {
        mDelivery = new Handler(Looper.getMainLooper());
        mRetryCount = 3;
        mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheMode = CacheMode.NO_CACHE;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        okHttpClient = builder.build();
    }

    /**
     * 示例，不做实际使用
     */
    private void sample(){
        //无论做什么请求，第一行的泛型一定要加！！！
        //无论做什么请求，第一行的泛型一定要加！！！
        //无论做什么请求，第一行的泛型一定要加！！！
        //可以是String类型，也可以是自定义的jsonbean
        OkGo.<ResultResponse>post("url")//请求响应的jsonbean与url
                .tag(this)//请求的tag 用于取消对应的请求
                .isMultipart(true) //强制使用multipart/form-data表单上传，一般不需调用
                .isSpliceUrl(true)//强制将params的参数拼接到url后面
                .retryCount(3)//超时重连次数
                .cacheKey("")//设置当前请求的缓存key
                .cacheTime(1000)//设置缓存过期时间
                .cacheMode(CacheMode.DEFAULT)//设置缓存模式
                .headers("key1", "value1")//添加请求头参数
                .headers("key2", "value2")//添加多个
                .params("param1", "paramValue1")//添加请求参数
                .params("param2", "paramValue2")//添加多个
                .params("file1", new File("path"))//添加文件
                .params("file2", new File("path"))//添加多个
                .addUrlParams("key", new ArrayList<String>())//一个key对应多个参数
                .addFileWrapperParams("key", new ArrayList<HttpParams.FileWrapper>())//一个key对应多个文件
                .execute(new JsonCallback<ResultResponse>() {
                    @Override
                    public void onStart(Request<ResultResponse, ? extends Request> request) {
                        super.onStart(request);
                        //UI线程，请求发起之前调用，可以showloading之类的处理，可以添加修改参数
                    }
                    @Override
                    public ResultResponse convertResponse(okhttp3.Response response) throws Throwable {
                        return super.convertResponse(response);
                        //子线程，可以做耗时操作
                        //解析数据返回需要的jsonbean对象
                        //可以自己定义异常情况在onError中处理
                    }
                    @Override
                    public void onSuccess(Response<ResultResponse> response) {
                        //UI线程。请求成功回调
                    }
                    @Override
                    public void onCacheSuccess(Response<ResultResponse> response) {
                        super.onCacheSuccess(response);
                        //UI线程。缓存读取成功的回调
                    }
                    @Override
                    public void onError(Response<ResultResponse> response) {
                        super.onError(response);
                        //UI线程。请求失败的回调
                    }
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        //UI线程。请求结束的回调，成功失败都会调用
                    }
                    @Override
                    public void uploadProgress(Progress progress) {
                        super.uploadProgress(progress);
                        //UI线程。文件上传进度回调，只有请求方式包含请求体才会调用
                        //注：请求体内容包括文件类型、大小等等数据
                    }
                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        //UI线程。文件下载进度回调
                    }
                });
        //获取图片数据
        OkGo.<Bitmap>get("")
                .tag(this)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Response<Bitmap> response) {

                    }
                });
        //下载文件
        OkGo.<File>get("")
                .tag(this)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {

                    }
                });
        //上传String类型的文本
        OkGo.<String>post("")
                .tag(this)
                .upString("要上传的文本数据")
                .upString("要上传的文本数据", MediaType.parse("application/xml"))//指定数据类型
                .execute(new StringCallback() {

                });
        //上传json文本 默认会携带以下请求头
        //Content-Type: application/json;charset=utf-8
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        JSONObject jsonObject = new JSONObject(map);
        OkGo.<String>post("")
                .tag(this)
                .upJson(jsonObject)
                .execute(new StringCallback() {

                });
        //上传文件
        //上传文件支持文件与参数一起同时上传，也支持一个key上传多个文件
        //OkGo自动添加请求头
        //Content-Type: multipart/form-data; boundary=f6b76bad-0345-4337-b7d8-b362cb1f9949
        //如果没有文件，那么OkGo将自动使用以下请求头
        //Content-Type: application/x-www-form-urlencoded
        //如果没有文件，你希望强制使用某个请求头如 multipart/form-data
        //那么可以使用.isMultipart(true)这个方法强制修改，一般来说是不需要强制的
    }

    public static OkGo getInstance() {
        return OkGoHolder.holder;
    }

    private static class OkGoHolder {
        private static OkGo holder = new OkGo();
    }

    /** get请求 */
    public static <T> GetRequest<T> get(String url) {
        return new GetRequest<>(url);
    }

    /** post请求 */
    public static <T> PostRequest<T> post(String url) {
        return new PostRequest<>(url);
    }

    /** put请求 */
    public static <T> PutRequest<T> put(String url) {
        return new PutRequest<>(url);
    }

    /** head请求 */
    public static <T> HeadRequest<T> head(String url) {
        return new HeadRequest<>(url);
    }

    /** delete请求 */
    public static <T> DeleteRequest<T> delete(String url) {
        return new DeleteRequest<>(url);
    }

    /** options请求 */
    public static <T> OptionsRequest<T> options(String url) {
        return new OptionsRequest<>(url);
    }

    /** patch请求 */
    public static <T> PatchRequest<T> patch(String url) {
        return new PatchRequest<>(url);
    }

    /** trace请求 */
    public static <T> TraceRequest<T> trace(String url) {
        return new TraceRequest<>(url);
    }

    /** 必须在全局Application先调用，获取context上下文，否则缓存无法使用 */
    public OkGo init(Application app) {
        context = app;
        return this;
    }

    /** 获取全局上下文 */
    public Context getContext() {
        HttpUtils.checkNotNull(context, "please call OkGo.getInstance().init() first in application!");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(okHttpClient, "please call OkGo.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /** 必须设置 */
    public OkGo setOkHttpClient(OkHttpClient okHttpClient) {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /** 获取全局的cookie实例 */
    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) okHttpClient.cookieJar();
    }

    /** 超时重试次数 */
    public OkGo setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /** 超时重试次数 */
    public int getRetryCount() {
        return mRetryCount;
    }

    /** 全局的缓存模式 */
    public OkGo setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /** 获取全局的缓存模式 */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /** 全局的缓存过期时间 */
    public OkGo setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /** 获取全局的缓存过期时间 */
    public long getCacheTime() {
        return mCacheTime;
    }

    /** 获取全局公共请求参数 */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /** 添加全局公共请求参数 */
    public OkGo addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /** 获取全局公共请求头 */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /** 添加全局公共请求参数 */
    public OkGo addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /** 添加全局公共请求参数 */
    public void removeCommonHeaders(String key) {
        if (mCommonHeaders != null) mCommonHeaders.remove(key);
    }

    /** 根据Tag包含的标识取消请求 */
    public void cancelTag(String tag) {
        if (StrUtils.isNull(tag)) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if(call.request().tag() instanceof String){
                String callTag = (String) call.request().tag();
                if (callTag.contains(tag)) {
                    call.cancel();
                }
            }else{
                cancelTag(call.request().tag());
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if(call.request().tag() instanceof String){
                String callTag = (String) call.request().tag();
                if (callTag.contains(tag)) {
                    call.cancel();
                }
            }else{
                cancelTag(call.request().tag());
            }
        }
    }

    /** 根据Tag取消请求 */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 根据Tag取消请求 */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 取消所有请求 */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /** 取消所有请求 */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

}
