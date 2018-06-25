package com.cheng.okgo.http.okgo.callback;

import com.cheng.okgo.StrUtils;
import com.cheng.okgo.http.okgo.convert.JsonConvert;
import com.cheng.okgo.http.okgo.exception.MyResponseException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okhttp3.Response;

public abstract class JsonCallback<T> extends AbsCallback<T>{

    private Type type;
    private Class<T> clazz;

    public JsonCallback() {

    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }
        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    @Override
    public void onError(com.cheng.okgo.http.okgo.model.Response<T> response) {
        if(response.getException()!=null && !(response.getException() instanceof MyResponseException)){
            if(StrUtils.isNull(response.message())){
                response.setException(new MyResponseException(response.code(), MyResponseException.getErrorMessage(response.code())));
            }else{
                response.setException(new MyResponseException(response.code(), response.message()));
            }
        }
        super.onError(response);
    }

}
