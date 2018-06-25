package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import com.cheng.okgo.http.okgo.model.Response;
import com.cheng.okgo.http.rx2.observable.BodyObservable;
import io.reactivex.Observable;

public class ObservableBody<T> implements CallAdapter<T, Observable<T>> {

    @Override
    public Observable<T> adapt(Call<T> call, AdapterParam param) {
        Observable<Response<T>> observable = AnalysisParams.analysis(call, param);
        return new BodyObservable<>(observable);
    }

}
