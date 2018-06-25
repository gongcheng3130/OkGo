package com.cheng.okgo.http.okgo.exception;

import com.cheng.okgo.http.BaseBean;

public class MyErrorCode extends BaseBean {

    //服务端响应code
    // 400 请求出错  由于语法格式有误，服务器无法理解此请求。不作修改，客户程序就无法重复此请求。
    public static final int RESPONSE_400 = 400;//网络请求出错，请联系客服
    // 401 未授权：登录失败
    public static final int RESPONSE_401 = 401;//登录过期，请重新登录
    // 402 需要付费
    public static final int RESPONSE_402 = 402;//未知错误
    // 403 禁止：禁止执行访问
    public static final int RESPONSE_403 = 403;//此操作没有权限
    // 404 找不到资源 Web 服务器找不到您所请求的文件或脚本。请检查URL 以确保路径正确。
    public static final int RESPONSE_404 = 404;//找不到路径或资源不存在
    // 405 不允许此方法 对于请求所标识的资源，不允许使用请求行中所指定的方法。请确保为所请求的资源设置了正确的 MIME 类型。
    public static final int RESPONSE_405 = 405;//未知错误
    // 406 不可接受 根据此请求中所发送的“接受”标题，此请求所标识的资源只能生成内容特征为“不可接受”的响应实体。
    public static final int RESPONSE_406 = 406;//未知错误
    // 407 需要代理身份验证 在可为此请求提供服务之前，您必须验证此代理服务器。请登录到代理服务器，然后重试
    public static final int RESPONSE_407 = 407;//未知错误
    // 408 访问超时
    public static final int RESPONSE_408 = 408;//未知错误
    // 409 冲突
    public static final int RESPONSE_409 = 409;//未知错误
    // 410 失败
    public static final int RESPONSE_410 = 410;//未知错误
    // 411 需要长度
    public static final int RESPONSE_411 = 411;//未知错误
    // 412 前提条件失败
    public static final int RESPONSE_412 = 412;//未知错误
    // 413 请求实体太大
    public static final int RESPONSE_413 = 413;//未知错误
    // 414 Request-URI 太长   Request-URL太长，服务器拒绝服务此请求。
    public static final int RESPONSE_414 = 414;//未知错误
    // 415 不支持媒体类型
    public static final int RESPONSE_415 = 415;//未知错误
    // 500 服务器的内部错误   Web 服务器不能执行此请求。请稍后重试此请求。
    public static final int RESPONSE_500 = 500;//服务暂不可用，请联系客服
    // 501 未实现  Web 服务器不支持实现此请求所需的功能。请检查URL中的错误
    public static final int RESPONSE_501 = 501;//服务暂不可用，请联系客服
    // 502 网关出错  当用作网关或代理时，服务器将从试图实现此请求时所访问的upstream 服务器中接收无效的响应。
    public static final int RESPONSE_502 = 502;//链接已失效
    // 503 此次请求找不到可用的资源
    public static final int RESPONSE_503 = 503;//服务不可用
    // 504 网关超时
    public static final int RESPONSE_504 = 504;//与服务器连接超时
    // 505 HTTP版本不支持
    public static final int RESPONSE_505 = 505;//网络请求错误，请尝试切换网络重试
    public static final int RESPONSE_OTHER = 600;//未知错误

    //自定义code
    public static final int CUSTOM_UNKNOWN = 1000;//未知错误
    public static final int CUSTOM_ILLEGAL = 1001;//自定义错误
    public static final int CUSTOM_PARSE_ERROR = 1002;//解析错误
    public static final int CUSTOM_NETWORD_ERROR = 1003;//网络错误
    public static final int CUSTOM_TIME_OUT = 1004;//连接超时
    public static final int CUSTOM_STORAGE = 1005;//SD卡不存在或没有权限
    public static final int CACHEKEY_EXPIRED = 1006;//缓存过期
    public static final int ALREADY_EXECUTED = 1007;//请求已执行
    public static final int CUSTOM_SSL_ERROR = 1008;//证书出错

}
