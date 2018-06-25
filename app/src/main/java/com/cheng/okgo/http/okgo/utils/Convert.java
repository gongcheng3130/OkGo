package com.cheng.okgo.http.okgo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Convert {

    private static Gson gson = null;

    private Convert() {

    }

    //build中一些常用方法
//					gson = new GsonBuilder()
//							.generateNonExecutableJson()//生成不可执行的Json（多了 )]}' 这4个字符）
//							.disableHtmlEscaping()//禁止转义html标签
//							.serializeNulls()//保留空值
//							.setPrettyPrinting() //对json结果格式化.
//							.excludeFieldsWithModifiers(Modifier.PRIVATE)//对某类修饰词所修饰的不做解析
//							.setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)//设置对Long类型的变量DEFAULT解析为long STRING解析为String
//							.excludeFieldsWithoutExposeAnnotation() //不解析@Expose修饰的字段
//							.enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
//							.generateNonExecutableJson()//生成的json字符串不具备正确格式的json字符串
//							.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式
//							.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
//							.setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
//							//@Since(版本号)能完美地实现这个功能.还有的字段可能,随着版本的升级而删除,那么
//							//@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
//							//.setExclusionStrategies(new ExclusionStrategy())//自定义排序不需要解析的字段
//							//.registerTypeAdapterFactory(new TypeAdapterFactory())//自己注册一个类型适配器，重写write和read方法，然后注册到适配器工厂
//							.create();
    private static Gson getInstance() {
        synchronized (Convert.class) {
            if(gson == null){
                gson = new GsonBuilder()
//                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                        .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                        .serializeNulls()//序列化null
                        .setDateFormat("yyyy-MM-dd")// 设置日期时间格式，另有2个重载方法, 在序列化和反序化时均生效
//                        .disableInnerClassSerialization()// 禁此序列化内部类
//                        .generateNonExecutableJson()//生成不可执行的Json（多了 )]}' 这4个字符）
                        .disableHtmlEscaping()//禁止转义html标签
                        .setPrettyPrinting()//格式化输出
                        .create();
            }
        }
        return gson;
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonIOException, JsonSyntaxException {
        return getInstance().fromJson(json, type);
    }

    public static <T> List<T> jsonToList(String json, Class<T[]> type) throws JsonIOException, JsonSyntaxException {
        T[] array = getInstance().fromJson(json, type);
        return Arrays.asList(array);
    }

    public static <T> ArrayList<T> jsonToArray(String json, Class<T> type) throws JsonIOException, JsonSyntaxException {
        Type t = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        ArrayList<JsonObject> jsonObjects = getInstance().fromJson(json, t);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects){
            arrayList.add(new Gson().fromJson(jsonObject, type));
        }
        return arrayList;
    }

    public static <T> T fromJson(String json, Type type) {
        return getInstance().fromJson(json, type);
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return getInstance().fromJson(reader, typeOfT);
    }

    public static <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return getInstance().fromJson(json, classOfT);
    }

    public static <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return getInstance().fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return getInstance().toJson(src);
    }

    public static String toJson(Object src, Type typeOfSrc) {
        return getInstance().toJson(src, typeOfSrc);
    }

    public static String formatJson(String json) {
        try {
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(json);
            return getInstance().toJson(je);
        } catch (Exception e) {
            return json;
        }
    }

    public static String formatJson(Object src) {
        try {
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(toJson(src));
            return getInstance().toJson(je);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
