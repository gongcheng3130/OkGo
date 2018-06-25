package com.cheng.okgo;

import com.cheng.okgo.http.BaseBean;

public class CheckVersion extends BaseBean{

    public int id;
    public String title;//更新标题
    public String content;//更新内容
    public String update_type;//daily_update 日常 important_update 重要 temporary_update 强更
    public String android_version_num;//1.7.5
    public String ios_version_num;//1.7.5
    public int android_num;//75
    public String ios_num;//75
    public String created_at;//
    public String updated_at;//
    public boolean isShow;//此版本是否提示过更新

}
