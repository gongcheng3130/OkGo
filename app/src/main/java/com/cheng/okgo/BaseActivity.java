package com.cheng.okgo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.cheng.okgo.http.okgo.OkGo;

/**
 * activity 基类
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    protected MyProgressDialog progressDialog;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideWaitDialog();
    }

    @Override
    protected void onDestroy() {
        hideWaitDialog();
        OkGo.getInstance().cancelTag(getClass().getSimpleName());
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    public boolean isDialogShowing() {
        return progressDialog == null ? false : progressDialog.isShowing();
    }

    public void hideWaitDialog() {
        if (progressDialog != null && isDialogShowing()) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (RuntimeException e) {
                Log.i("111", "弹窗出异常啦 --- BaseActivity.hideWaitDialog() = " + getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }

    public void showWaitDialog() {
        showWaitDialog("加载中...");
    }

    public void showWaitDialog(int resid) {
        showWaitDialog(getString(resid));
    }

    public void showWaitDialog(String text) {
        if (!isDialogShowing() && !isDestroyed() && !isFinishing() && !getFragmentManager().isDestroyed()) {
            if (progressDialog == null) progressDialog = new MyProgressDialog(this, text);
            try {
                progressDialog.show();
            } catch (RuntimeException e) {
                Log.i("111", "弹窗出异常啦 --- BaseActivity.showWaitDialog() = " + getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }

}
