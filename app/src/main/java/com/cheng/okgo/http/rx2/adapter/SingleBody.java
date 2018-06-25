package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import io.reactivex.Single;

public class SingleBody<T> implements CallAdapter<T, Single<T>> {

    public Single<T> adapt(Call<T> call, AdapterParam param) {
        ObservableBody<T> observable = new ObservableBody<>();
        return observable.adapt(call, param).singleOrError();
    }

}
