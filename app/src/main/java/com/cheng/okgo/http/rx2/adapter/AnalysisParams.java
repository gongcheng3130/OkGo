package com.cheng.okgo.http.rx2.adapter;


import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.model.Response;
import com.cheng.okgo.http.rx2.observable.CallEnqueueObservable;
import com.cheng.okgo.http.rx2.observable.CallExecuteObservable;

import io.reactivex.Observable;

class AnalysisParams {

    static <T> Observable<Response<T>> analysis(Call<T> call, AdapterParam param) {
        Observable<Response<T>> observable;
        if (param == null) param = new AdapterParam();
        if (param.isAsync) {
            observable = new CallEnqueueObservable<>(call);
        } else {
            observable = new CallExecuteObservable<>(call);
        }
        return observable;
    }
}
