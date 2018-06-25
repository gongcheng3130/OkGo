package com.cheng.okgo.http.okgo.callback;

import com.cheng.okgo.http.okgo.convert.StringConvert;
import com.cheng.okgo.http.okgo.exception.MyResponseException;
import okhttp3.Response;

public abstract class StringCallback extends AbsCallback<String> {

    public StringConvert convert;

    public StringCallback() {
        convert = new StringConvert();
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        return s;
    }

    @Override
    public void onError(com.cheng.okgo.http.okgo.model.Response<String> response) {
        if(response.getException()!=null && !(response.getException() instanceof MyResponseException)){
            response.setException(new MyResponseException(response.code(), response.message()));
        }
        super.onError(response);
    }

}
