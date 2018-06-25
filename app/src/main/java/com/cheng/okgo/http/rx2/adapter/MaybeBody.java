package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import io.reactivex.Maybe;

public class MaybeBody<T> implements CallAdapter<T, Maybe<T>> {

    @Override
    public Maybe<T> adapt(Call<T> call, AdapterParam param) {
        ObservableBody<T> observable = new ObservableBody<>();
        return observable.adapt(call, param).singleElement();
    }

}
