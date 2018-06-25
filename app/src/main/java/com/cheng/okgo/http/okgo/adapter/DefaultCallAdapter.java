package com.cheng.okgo.http.okgo.adapter;

public class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {

    @Override
    public Call<T> adapt(Call<T> call, AdapterParam param) {
        return call;
    }

}
