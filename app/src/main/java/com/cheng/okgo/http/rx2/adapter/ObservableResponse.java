package com.cheng.okgo.http.rx2.adapter;

import com.cheng.okgo.http.okgo.adapter.AdapterParam;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.adapter.CallAdapter;
import com.cheng.okgo.http.okgo.model.Response;
import io.reactivex.Observable;

public class ObservableResponse<T> implements CallAdapter<T, Observable<Response<T>>> {

    @Override
    public Observable<Response<T>> adapt(Call<T> call, AdapterParam param) {
        return AnalysisParams.analysis(call, param);
    }

}
