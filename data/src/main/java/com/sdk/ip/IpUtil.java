package com.sdk.ip;

import android.app.Activity;
import android.content.Context;

public abstract class IpUtil {

    protected static IpUtil instance;

    private String mVersionName = "1.0.0";
    private int mVersionCode = 1;

    public static IpUtil getInstance() {
        if (instance == null) {
            synchronized (IpUtil.class) {
                instance = new IpContent();
            }
        }
        return instance;
    }


    public abstract void init(Context context);

    public abstract void loadData(Activity activity);

    public abstract void channel(String channel);

    public abstract void secretKey(String key);

    public abstract boolean isVpnUsed();

    public abstract int getIp_type();

    public abstract void onDestroy();

}
