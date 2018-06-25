package com.cheng.okgo.http.okgo.adapter;

import com.cheng.okgo.http.okgo.callback.Callback;
import com.cheng.okgo.http.okgo.model.Response;
import com.cheng.okgo.http.okgo.request.base.Request;

public interface Call<T> {
    /** 同步执行 */
    Response<T> execute() throws Exception;

    /** 异步回调执行 */
    void execute(Callback<T> callback);

    /** 是否已经执行 */
    boolean isExecuted();

    /** 取消 */
    void cancel();

    /** 是否取消 */
    boolean isCanceled();

    Call<T> clone();

    Request getRequest();

}
