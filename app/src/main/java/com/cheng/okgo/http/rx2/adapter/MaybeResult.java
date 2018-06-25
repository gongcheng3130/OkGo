package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import com.cheng.okgo.http.okgo.model.Result;
import io.reactivex.Maybe;

public class MaybeResult<T> implements CallAdapter<T, Maybe<Result<T>>> {

    @Override
    public Maybe<Result<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResult<T> observable = new ObservableResult<>();
        return observable.adapt(call, param).singleElement();
    }

}
