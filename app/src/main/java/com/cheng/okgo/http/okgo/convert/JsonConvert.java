package com.cheng.okgo.http.okgo.convert;

import com.cheng.okgo.StrUtils;
import com.cheng.okgo.http.ResultResponse;
import com.cheng.okgo.http.SimpleResponse;
import com.cheng.okgo.http.okgo.exception.MyResponseException;
import com.cheng.okgo.http.okgo.utils.Convert;
import com.google.gson.stream.JsonReader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }
        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (rawType == String.class) {
            //noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(body.string());
        } else {
            T t = Convert.fromJson(jsonReader, rawType);
            response.close();
            return checkClass(response, t);
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());
        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = Convert.fromJson(jsonReader, type);
        response.close();
        return checkClass(response, t);
    }

    private T checkClass(Response response, T t) throws Exception {
        if(t instanceof ResultResponse){
            if(response.code()==200){
                int code = ((ResultResponse)t).code;
                if(code == 0){
                    return t;
                }else{
                    throw new MyResponseException(code, ((ResultResponse)t).message);
                }
            }else{
                throw new MyResponseException(response.code(), ((ResultResponse)t).message);
            }
        }else{
            return t;
        }
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if(rawType == ResultResponse.class){
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<ResultResponse<Void>>(this)
                SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
                response.close();
                //noinspection unchecked
                if(response.code()==200){
                    int code = simpleResponse.code;
                    if(code == 0){
                        return (T) simpleResponse.toResultResponse();
                    }else{
                        throw new MyResponseException(code, simpleResponse.message);
                    }
                }else{
                    //直接将服务端的错误信息抛出，onError中可以获取
                    throw new MyResponseException(response.code(), MyResponseException.getErrorMessage(response.code()));
                }
            } else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                ResultResponse result = Convert.fromJson(jsonReader, type);
                response.close();
                //这里的0是以下意思
                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
                if(response.code()==200){
                    int code = result.code;
                    String error;
                    if(StrUtils.isNotNull(result.error)){
                        error = result.error;
                    }else{
                        error = result.message;
                    }
                    if(code == 0){
                        return (T) result;
                    }else{
                        throw new MyResponseException(code, error);
                    }
                }else{
                    //直接将服务端的错误信息抛出，onError中可以获取
                    throw new MyResponseException(response.code(), MyResponseException.getErrorMessage(response.code()));
                }
            }
        } else {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = Convert.fromJson(jsonReader, type);
            response.close();
            if(response.code()==200){
                return t;
            }else{
                throw new MyResponseException(response.code(), MyResponseException.getErrorMessage(response.code()));
            }
        }
    }

}
