package com.cheng.okgo.http;

/**
 * 基础业务逻辑
 */
public class SimpleResponse extends BaseBean {

    public int code;
    public String message;

    @Override
    public String toString() {
        return "ResultResponse{\n" +//
               "\tcode=" + code + "\n" +//
               "\tmessage='" + message + "\'\n" +//
               '}';
    }

    public ResultResponse toResultResponse() {
        ResultResponse result = new ResultResponse();
        result.code = code;
        result.message = message;
        return result;
    }

}
