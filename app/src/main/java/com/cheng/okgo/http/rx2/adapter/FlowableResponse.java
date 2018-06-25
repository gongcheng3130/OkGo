package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import com.cheng.okgo.http.okgo.model.Response;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class FlowableResponse<T> implements CallAdapter<T, Flowable<Response<T>>> {

    @Override
    public Flowable<Response<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResponse<T> observable = new ObservableResponse<>();
        return observable.adapt(call, param).toFlowable(BackpressureStrategy.LATEST);
    }

}
