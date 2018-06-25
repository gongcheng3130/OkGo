package com.cheng.okgo;

import com.cheng.okgo.http.BaseBean;
import java.util.Random;

public class ApkModel extends BaseBean{

    public String name;
    public String url;
    public String iconUrl;
    public int priority;

    public ApkModel() {
        Random random = new Random();
        priority = random.nextInt(100);
    }

}
