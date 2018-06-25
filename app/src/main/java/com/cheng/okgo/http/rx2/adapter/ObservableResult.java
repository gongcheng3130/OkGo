package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import com.cheng.okgo.http.okgo.model.Response;
import com.cheng.okgo.http.okgo.model.Result;
import com.cheng.okgo.http.rx2.observable.ResultObservable;
import io.reactivex.Observable;

public class ObservableResult<T> implements CallAdapter<T, Observable<Result<T>>> {

    @Override
    public Observable<Result<T>> adapt(Call<T> call, AdapterParam param) {
        Observable<Response<T>> observable = AnalysisParams.analysis(call, param);
        return new ResultObservable<>(observable);
    }

}
