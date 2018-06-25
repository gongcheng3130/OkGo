package com.cheng.okgo.http.okgo.request.base;

import com.cheng.okgo.http.okgo.utils.HttpUtils;
import okhttp3.RequestBody;

public abstract class NoBodyRequest<T, R extends NoBodyRequest> extends Request<T, R> {

    public NoBodyRequest(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    protected okhttp3.Request.Builder generateRequestBuilder(RequestBody requestBody) {
        url = HttpUtils.createUrlFromParams(baseUrl, params.urlParamsMap);
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        return HttpUtils.appendHeaders(requestBuilder, headers);
    }

}
