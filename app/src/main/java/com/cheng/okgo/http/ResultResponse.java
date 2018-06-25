package com.cheng.okgo.http;

/**
 * 基础业务逻辑
 */
public class ResultResponse<T> extends BaseBean {

    public int code;
    public String message;
    public T data;
    public String error;//接口请求特殊错误

    @Override
    public String toString() {
        return "ResultResponse{\n" +//
               "\tcode=" + code + "\n" +//
               "\tmessage='" + message + "\'\n" +//
               "\tdata=" + data + "\n" +//
               '}';
    }

}
