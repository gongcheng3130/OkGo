package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import com.cheng.okgo.http.okgo.model.Result;
import io.reactivex.Single;

public class SingleResult<T> implements CallAdapter<T, Single<Result<T>>> {
    @Override
    public Single<Result<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResult<T> observable = new ObservableResult<>();
        return observable.adapt(call, param).singleOrError();
    }

}
