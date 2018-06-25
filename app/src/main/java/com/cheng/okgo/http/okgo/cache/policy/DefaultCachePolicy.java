package com.cheng.okgo.http.okgo.cache.policy;

import com.cheng.okgo.http.okgo.cache.CacheEntity;
import com.cheng.okgo.http.okgo.callback.Callback;
import com.cheng.okgo.http.okgo.exception.MyErrorCode;
import com.cheng.okgo.http.okgo.exception.MyResponseException;
import com.cheng.okgo.http.okgo.model.Response;
import com.cheng.okgo.http.okgo.request.base.Request;
import okhttp3.Call;

public class DefaultCachePolicy<T> extends BaseCachePolicy<T> {

    public DefaultCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(success);
                mCallback.onFinish();
            }
        });
    }

    @Override
    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(error);
                mCallback.onFinish();
            }
        });
    }

    @Override
    public boolean onAnalysisResponse(final Call call, final okhttp3.Response response) {
        if (response.code() != 304) return false;

        if (cacheEntity == null) {
//            the http response code is 304,  but the cache with cacheKey is null or expired!
            final Response<T> error = Response.error(true, call, response, new MyResponseException(MyErrorCode.CACHEKEY_EXPIRED), request.getTag());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(error);
                    mCallback.onFinish();
                }
            });
        } else {
            final Response<T> success = Response.success(true, cacheEntity.getData(), call, response, request.getTag());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallback.onCacheSuccess(success);
                    mCallback.onFinish();
                }
            });
        }
        return true;
    }

    @Override
    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
        } catch (Throwable throwable) {
            return Response.error(false, rawCall, null, throwable, request.getTag());
        }
        Response<T> response = requestNetworkSync();
        //HTTP cache protocol
        if (response.isSuccessful() && response.code() == 304) {
            if (cacheEntity == null) {
//                the http response code is 304,  but the cache with cacheKey is null or expired!
                response = Response.error(true, rawCall, response.getRawResponse(), new MyResponseException(MyErrorCode.CACHEKEY_EXPIRED), request.getTag());
            } else {
                response = Response.success(true, cacheEntity.getData(), rawCall, response.getRawResponse(), request.getTag());
            }
        }
        return response;
    }

    @Override
    public void requestAsync(CacheEntity<T> cacheEntity, Callback<T> callback) {
        mCallback = callback;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onStart(request);

                try {
                    prepareRawCall();
                } catch (Throwable throwable) {
                    Response<T> error = Response.error(false, rawCall, null, throwable, request.getTag());
                    mCallback.onError(error);
                    return;
                }
                requestNetworkAsync();
            }
        });
    }

}
