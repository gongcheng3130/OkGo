package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import com.cheng.okgo.http.okgo.model.Result;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class FlowableResult<T> implements CallAdapter<T, Flowable<Result<T>>> {

    @Override
    public Flowable<Result<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResult<T> observable = new ObservableResult<>();
        return observable.adapt(call, param).toFlowable(BackpressureStrategy.LATEST);
    }

}
