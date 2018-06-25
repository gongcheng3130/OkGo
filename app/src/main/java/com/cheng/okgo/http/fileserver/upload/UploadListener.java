package com.cheng.okgo.http.fileserver.upload;

import com.cheng.okgo.http.fileserver.ProgressListener;

public abstract class UploadListener<T> implements ProgressListener<T> {

    public final Object tag;

    public UploadListener(Object tag) {
        this.tag = tag;
    }
}
