package com.cheng.okgo.http.fileserver.download;

import com.cheng.okgo.http.fileserver.ProgressListener;
import java.io.File;

public abstract class DownloadListener implements ProgressListener<File> {

    public final Object tag;

    public DownloadListener(Object tag) {
        this.tag = tag;
    }

}
