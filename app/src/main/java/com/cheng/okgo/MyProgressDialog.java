package com.cheng.okgo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class MyProgressDialog extends Dialog {

    private Context context;
    private String progressText;
    public boolean cancelable = true;
    private TextView title;

    public MyProgressDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }

    public MyProgressDialog(Context context, String progressText) {
        super(context, R.style.dialog);
        this.context = context;
        this.progressText = progressText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        GifView gif = (GifView) findViewById(R.id.iv_progress);
        gif.setMovieResource(R.drawable.loading);
        title = (TextView) findViewById(R.id.custom_imageview_progress_title);
        title.setText(progressText == null ? "加载中..." : progressText);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0 && isShowing() && !cancelable){
            ((BaseActivity)context).finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setMessage(String message) {
        progressText = message;
    }

    @Override
    public void show() {
        try{
            super.show();
            title.setText(StrUtils.isNull(progressText) ? "加载中..." : progressText);
        }catch (RuntimeException e){
            Log.i("111", "弹窗出异常啦 --- MyProgressDialog.show() = " + context.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try{
            super.dismiss();
        }catch (RuntimeException e){
            Log.i("111", "弹窗出异常啦 --- MyProgressDialog.dismiss() = " + context.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

}
