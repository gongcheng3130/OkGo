package com.cheng.okgo;

import android.app.Application;
import com.cheng.okgo.http.okgo.OkGo;
import com.cheng.okgo.http.okgo.cache.CacheMode;
import com.cheng.okgo.http.okgo.cookie.CookieJarImpl;
import com.cheng.okgo.http.okgo.cookie.store.MemoryCookieStore;
import com.cheng.okgo.http.okgo.https.HttpsUtils;
import com.cheng.okgo.http.okgo.interceptor.HttpLoggingInterceptor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import okhttp3.OkHttpClient;

public class MyApplication extends Application{

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initOkGo();
    }

    private void initOkGo() {
        //1. 初始化
        //如果不需要配置直接使用默认，则只需要下面这一句代码，之后的代码可以不用
//        OkGo.getInstance().init(this);

        //2. 构建OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //3. 配置log 如下使用的是OkGo自带log，也可以自己写
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //4. 配置超时时间
        //默认都是60秒，也即最坏情况下可能3分钟才得到连接
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        //5. 配置Cookie
        //使用内存保持cookie，app退出后，cookie消失
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        //6. Https配置
        //信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        builder.sslSocketFactory(HttpsUtils.getSslSocketFactory().sSLSocketFactory, HttpsUtils.getSslSocketFactory().trustManager);
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

        //7. 配置OkGo通用请求头参数
//        HttpHeaders headers = new HttpHeaders();
//        String token = ConstantsToken.getInstance().getToken(this);
//        if (StrUtils.isNotNull(token)) {
//            headers.put("Authorization", "Token token=" + ConstantsToken.getInstance().getToken(this));//header不支持中文，不允许有特殊字符
//        }
        // 其他统一的配置
        OkGo.getInstance()
                .init(this)
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setRetryCount(0)     ;                          //全局统一超时重连次数，不需要可以设置为0
//                .addCommonHeaders(headers);                     //全局公共头
    }

}
