package com.cheng.okgo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.cheng.okgo.http.ResultResponse;
import com.cheng.okgo.http.fileserver.OkUpload;
import com.cheng.okgo.http.fileserver.upload.UploadTask;
import com.cheng.okgo.http.okgo.OkGo;
import com.cheng.okgo.http.okgo.adapter.Call;
import com.cheng.okgo.http.okgo.callback.FileCallback;
import com.cheng.okgo.http.okgo.callback.JsonCallback;
import com.cheng.okgo.http.okgo.model.Progress;
import com.cheng.okgo.http.okgo.model.Response;
import com.cheng.okgo.http.okgo.request.base.Request;
import java.io.File;
import java.util.Random;

public class MainActivity extends BaseActivity {

    private TextView tv1, tv2, tv3, tv4;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    hideWaitDialog();
                    tv2.setText((String)msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt1).setOnClickListener(this);
        tv1 = findViewById(R.id.tv1);
        findViewById(R.id.bt2).setOnClickListener(this);
        tv2 = findViewById(R.id.tv2);
        findViewById(R.id.bt3).setOnClickListener(this);
        tv3 = findViewById(R.id.tv3);
        findViewById(R.id.bt4).setOnClickListener(this);
        tv4 = findViewById(R.id.tv4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                asynRequest();
                break;
            case R.id.bt2:
                synRequest();
                break;
            case R.id.bt3:
                if(checkSDCardPermission()){
                    download();
                }else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
                break;
            case R.id.bt4:
                if(checkSDCardPermission()){
                    upload();
                }else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                }
                break;
        }
    }

    /** 检查SD卡权限 */
    protected boolean checkSDCardPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(requestCode==100)download();
                if(requestCode==200)upload();
            } else {
                Toast.makeText(this, "权限被禁止，无法下载文件！", Toast.LENGTH_SHORT);
            }
        }
    }

    private String url = "https://production.71gj.com.cn/api/v2/utils/app_update_log";

    private void asynRequest(){
        showWaitDialog();
        OkGo.<ResultResponse<CheckVersion>>get(url)
                .tag(getClass().getSimpleName() + "_get_version_info")
                .params("source_type", "Android")
                .execute(new JsonCallback<ResultResponse<CheckVersion>>() {
                    @Override
                    public void onError(Response<ResultResponse<CheckVersion>> response) {
                        super.onError(response);
                        tv1.setText("请求失败：" + response.getException().getMessage());
                    }
                    @Override
                    public void onSuccess(Response<ResultResponse<CheckVersion>> response) {
                        super.onSuccess(response);
                        tv1.setText("更新标题：" + response.body().data.title + "\n更新内容：" + response.body().data.content);
                    }
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideWaitDialog();
                    }
                });
    }

    private void synRequest(){
        showWaitDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Message m = handler.obtainMessage();
                    m.what = 0;
                    Call call = OkGo.<ResultResponse<CheckVersion>>get(url)
                            .tag(getClass().getSimpleName() + "_get_version_info")
                            .params("source_type", "Android")
                            .converter(new JsonCallback<ResultResponse<CheckVersion>>(){})
                            .adapt();
                    Response<ResultResponse<CheckVersion>> execute = call.execute();
                    if(execute.getException()!=null){
                        m.obj = "请求失败：" + execute.getException().getMessage();
                    }else{
                        m.obj = "更新标题：" + execute.body().data.title + "\n更新内容：" + execute.body().data.content;
                    }
                    handler.sendMessage(m);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void download(){
        ApkModel apk = new ApkModel();
        apk.name = "爱奇艺";
        apk.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c10c4c0155c9adf1282af008ed329378d54112ac";
        apk.url = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk";

        //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
//        GetRequest<File> request = OkGo.<File>get(apk.url)//
//                .headers("aaa", "111")//
//                .params("bbb", "222");
        //正常文件下载
        OkGo.<File>get(apk.url)//
                .tag(apk.url)//
                .headers("header1", "headerValue1")//
                .params("param1", "paramValue1")//
                .execute(new FileCallback("aiqiyi.apk") {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        tv3.setText("开始下载");
                    }
                    @Override
                    public void onSuccess(Response<File> response) {
                        tv3.setText("下载完成");
                    }
                    @Override
                    public void onError(Response<File> response) {
                        tv3.setText("下载出错");
                    }
                    @Override
                    public void downloadProgress(Progress progress) {
                        String downloadLength = Formatter.formatFileSize(getApplicationContext(), progress.currentSize);
                        String totalLength = Formatter.formatFileSize(getApplicationContext(), progress.totalSize);
                        tv3.setText("下载进度：" + downloadLength + "/" + totalLength);
                    }
                });

        //以下是专门针对多文件下载使用OkDownload管理下载
//        OkDownload.request(apk.url, OkGo.<File>get(apk.url))//tag, 请求体
//                .priority(apk.priority)//任务优先级
//                .extra1(apk)//
//                .save()//
//                .register(new DownloadListener(apk.url) {
//                    @Override
//                    public void onStart(Progress progress) {
//                        tv3.setText("onStart: " + progress);
//                    }
//                    @Override
//                    public void onProgress(Progress progress) {
//                        tv3.setText("onProgress: " + progress);
//                    }
//                    @Override
//                    public void onError(Progress progress) {
//                        tv3.setText("onError: " + progress);
//                        progress.exception.printStackTrace();
//                    }
//                    @Override
//                    public void onFinish(File file, Progress progress) {
//                        tv3.setText("onFinish: " + progress);
//                    }
//                    @Override
//                    public void onRemove(Progress progress) {
//                        tv3.setText("onRemove: " + progress);
//                    }
//                })//回调监听
//                .start();
    }

    private void upload(){
        //正常文件上传
        OkGo.<ResultResponse>post("")//
                .tag(this)//
//                .headers("header1", "headerValue1")//
//                .params("param1", "paramValue1")//
//                .params("file1", new File("文件路径"))   //这种方式为一个key，对应一个文件
//                .addFileParams("file", files)       // 这种方式为同一个key，上传多个文件
                .execute(new JsonCallback<ResultResponse>() {
                    @Override
                    public void onStart(Request<ResultResponse, ? extends Request> request) {
                        tv4.setText("上传开始");
                    }
                    @Override
                    public void onSuccess(Response<ResultResponse> response) {
                        tv4.setText("上传完成");
                    }
                    @Override
                    public void onError(Response<ResultResponse> response) {
                        tv4.setText("上传出错");
                    }
                    @Override
                    public void uploadProgress(Progress progress) {
                        String downloadLength = Formatter.formatFileSize(getApplicationContext(), progress.currentSize);
                        String totalLength = Formatter.formatFileSize(getApplicationContext(), progress.totalSize);
                        tv4.setText("上传进度：" + downloadLength + "/" + totalLength);
                    }
                });

        //以下是专门针对多文件上传使用OkUpload管理下载
        //这里是演示可以传递任何数据
//        PostRequest<String> postRequest = OkGo.<String>post(Urls.URL_FORM_UPLOAD)//
//                .headers("aaa", "111")//
//                .params("bbb", "222")//
//                .params("fileKey" + i, new File(imageItem.path))//
//                .converter(new StringConvert());
        ImageItem imageItem = new ImageItem();
        UploadTask<String> task = OkUpload.request(imageItem.path, OkGo.<String>post(""))//
                .priority(new Random().nextInt(100))//
                .extra1(imageItem)//
                .save();

    }

}
