package com.cheng.okgo.http.okgo.convert;

import com.cheng.okgo.http.okgo.exception.MyResponseException;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StringConvert implements Converter<String> {

    @Override
    public String convertResponse(Response response) throws Throwable {
        if(response.code()==200){
            ResponseBody body = response.body();
            if (body == null) return null;
            return body.string();
        }else{
            throw new MyResponseException(response.code(), MyResponseException.getErrorMessage(response.code()));
        }
    }

}
