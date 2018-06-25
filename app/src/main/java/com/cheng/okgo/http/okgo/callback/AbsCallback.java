package com.cheng.okgo.http.okgo.callback;

import com.cheng.okgo.http.okgo.model.Progress;
import com.cheng.okgo.http.okgo.model.Response;
import com.cheng.okgo.http.okgo.request.base.Request;
import com.cheng.okgo.http.okgo.utils.OkLogger;

public abstract class AbsCallback<T> implements Callback<T> {

    @Override
    public void onStart(Request<T, ? extends Request> request) {

    }

    @Override
    public void onCacheSuccess(Response<T> response) {

    }

    @Override
    public void onSuccess(Response<T> response) {

    }

    @Override
    public void onError(Response<T> response) {
        OkLogger.printStackTrace(response.getException());
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }

}
