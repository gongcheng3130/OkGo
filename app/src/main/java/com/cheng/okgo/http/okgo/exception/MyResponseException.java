package com.cheng.okgo.http.okgo.exception;

import android.accounts.NetworkErrorException;
import android.net.ParseException;
import com.cheng.okgo.http.okgo.model.Response;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.json.JSONException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLHandshakeException;

public class MyResponseException extends RuntimeException {

    public int code;//HTTP status code
    public String message;//HTTP status message
    private transient Response<?> response;//The full HTTP response. This may be null if the exception was serialized

    public MyResponseException(Response<?> response){
        this.code = response.code();
        this.message = getErrorMessage(code);
        this.response = response;
    }

    public MyResponseException(int code){
        this.code = code;
        this.message = getErrorMessage(code);
    }

    public MyResponseException(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MyResponseException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public static MyResponseException handleException(Throwable throwable) {
        MyResponseException ex;
        if (throwable instanceof MyResponseException) {//业务逻辑的错误
            ex = (MyResponseException) throwable;
        }  else if (throwable instanceof IllegalStateException) {//自定义的错误
            ex = new MyResponseException(MyErrorCode.CUSTOM_ILLEGAL, getErrorMessage(MyErrorCode.CUSTOM_ILLEGAL));
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof IllegalStateException
                || throwable instanceof JsonSyntaxException
                || throwable instanceof ParseException) {//返回的数据解析错误
            ex = new MyResponseException(MyErrorCode.CUSTOM_PARSE_ERROR, getErrorMessage(MyErrorCode.CUSTOM_PARSE_ERROR));
        } else if (throwable instanceof ConnectException
                || throwable instanceof UnknownHostException
                || throwable instanceof NetworkErrorException) {//网络连接失败
            ex = new MyResponseException(MyErrorCode.CUSTOM_NETWORD_ERROR, getErrorMessage(MyErrorCode.CUSTOM_NETWORD_ERROR));
        } else if (throwable instanceof SocketTimeoutException) {//网络连接超时
            ex = new MyResponseException(MyErrorCode.CUSTOM_TIME_OUT, getErrorMessage(MyErrorCode.CUSTOM_TIME_OUT));
        } else if (throwable instanceof SSLHandshakeException) {//https请求如果有证书验证失败
            ex = new MyResponseException(MyErrorCode.CUSTOM_SSL_ERROR, getErrorMessage(MyErrorCode.CUSTOM_SSL_ERROR));
        }else {
            ex = new MyResponseException(MyErrorCode.CUSTOM_UNKNOWN, getErrorMessage(MyErrorCode.CUSTOM_UNKNOWN));
        }
        return ex;
    }

    public static String getErrorMessage(int code){
        String error;
        if(code==MyErrorCode.RESPONSE_400){
            error = "网络请求出错，请联系客服";
        }else if(code==MyErrorCode.RESPONSE_401){
            error = "登录过期，请重新登录";
        }else if(code==MyErrorCode.RESPONSE_403){
            error = "此操作没有权限";
        }else if(code==MyErrorCode.RESPONSE_404){
            error = "找不到路径或资源不存在";
        }else if(code==MyErrorCode.RESPONSE_501){
            error = "服务不可用";
        }else if(code==MyErrorCode.RESPONSE_502){
            error = "链接已失效";
        }else if(code==MyErrorCode.RESPONSE_503){
            error = "服务不可用";
        }else if(code==MyErrorCode.RESPONSE_504){
            error = "与服务器连接超时";
        }else if(code==MyErrorCode.RESPONSE_505){
            error = "网络请求错误，请尝试切换网络重试";
        }else if(code==MyErrorCode.CUSTOM_ILLEGAL){
            error = "请求失败，请稍后重试";
        }else if(code==MyErrorCode.CUSTOM_PARSE_ERROR){
            error = "获取数据异常，请联系客服";
        }else if(code==MyErrorCode.CUSTOM_NETWORD_ERROR){
            error = "网络连接失败，请确认网络是否正常";
        }else if(code==MyErrorCode.CUSTOM_TIME_OUT){
            error = "请求超时，请重试";
        }else if(code==MyErrorCode.CACHEKEY_EXPIRED){
            error = "缓存不存在或已过期";
        }else if(code==MyErrorCode.CUSTOM_STORAGE){
            error = "请确认是否存在SD卡并检查是否开启了相关权限";
        }else if(code==MyErrorCode.CUSTOM_SSL_ERROR){
            error = "证书验证失败";
        }else if(code==MyErrorCode.ALREADY_EXECUTED){
            error = "请求已执行";
        }else if(code>=404 && code<1000){
            error = "network error! http response code is 404 or 5xx!";
        }else{
            error = "网络连接失败，请稍后重试";
        }
        return error;
    }

}
