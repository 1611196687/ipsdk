package com.sdk.ip;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.google.gson.Gson;
import com.sdk.ip.bean.InitEntity;
import com.sdk.ip.bean.IpAddEntity;
import com.sdk.ip.net.JavaHttpRequest;
import com.sdk.ip.net.callback.BaseResponse;
import com.sdk.ip.net.callback.HttpCallback;
import com.sdk.ip.net.core.CBCUtil;
import com.sdk.ip.util.SPUtils;
import com.wandroid.traceroute.TraceRoute;
import com.wandroid.traceroute.TraceRouteCallback;
import com.wandroid.traceroute.TraceRouteResult;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.LOCATION_SERVICE;

public class IpContent extends IpUtil {

    public static Context mContext;
    private String[] split;
    private String ip;
    private List<String> ipList;
    private InitEntity object;

    private int networkType = 1;
    private boolean vpnUsed;
//    private static String deviceId;

    // 位置
    Location location = null;

    @Override
    public void init(Context context) {
        this.mContext = context;
        DeviceIdentifier.register((Application) context);
        getIp_type();
        vpnUsed = isVpnUsed();
    }

    @Override
    public void loadData(Activity activity) {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(mContext, Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            location = getLastKnownLocation();
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity, permissions, 10001);
        } else {
            location = getLastKnownLocation();
        }

        JavaHttpRequest.getInitInfo(new HttpCallback<InitEntity>() {
            @Override
            public void onSuccess(BaseResponse<InitEntity> response) {
                object = response.getObject();
                String server_ip_list = object.getServer_ip_list();
                split = server_ip_list.split(",");
                String agreement = SPUtils.get(mContext, "time", "").toString();
                Long time = System.currentTimeMillis() / 1000;
                if (!agreement.isEmpty()) {
                    if (time - Integer.parseInt(agreement) > Integer.parseInt(object.getSecond())) {
                        SPUtils.put(mContext, "time", time + "");
                        getAddIp();
                    }
                } else {
                    SPUtils.put(mContext, "time", time + "");
                    getAddIp();
                }


            }

            @Override
            public void onFailed(int code, String message) {
                LogUtils.d(code + message);
            }
        });
    }

    public static String channel;

    @Override
    public void channel(String channel) {
        this.channel = channel;
    }

    private void getAddIp() {
        Map<String, Object> map = new HashMap<>();
        map.put("ip_type", networkType);
        map.put("ip", object.getClient_ip());
        map.put("is_vpn", vpnUsed ? 1 : 2);
        map.put("task_id", object.getTask_id());
        map.put("longitude", location != null ? location.getLongitude() + "" : "");
        map.put("latitude", location != null ? location.getLatitude() + "" : "");
        if (location != null) {
            List<Address> addList = null;
            Geocoder geocoder = new Geocoder(mContext);
            try {
                addList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addList != null && addList.size() > 0) {
                    for (int i = 0; i < addList.size(); i++) {
                        Address ad = addList.get(i);
                        area = ad.getAddressLine(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        map.put("area", area);
        Log.d("area", area);
        String key = CBCUtil.encrypt(JSON.toJSONString(map));
        JavaHttpRequest.getIPInfoAdd(key, new HttpCallback<InitEntity>() {
            @Override
            public void onSuccess(BaseResponse<InitEntity> response) {
            }

            @Override
            public void onFailed(int code, String message) {
            }
        });
        getDataip(0);
    }

//    public static String getDeviceId() {
//        return deviceId;
//    }

    private void getDataip(final int i) {
        if (i < split.length) {
            ip = split[i];
            TraceRoute.INSTANCE.traceRoute(ip, true);
            TraceRoute.INSTANCE.setCallback(new TraceRouteCallback() {
                @Override
                public void onSuccess(@NotNull TraceRouteResult traceRouteResult) {
                    ipList = new ArrayList<>();
                    getIp(traceRouteResult, i);
//                    getDataip(i + 1);
                }

                @Override
                public void onUpdate(@NotNull String s) {
                }

                @Override
                public void onFailed(int i, @NotNull String s) {
                }
            });
        }
    }

    private String area = "";

    private void getIp(TraceRouteResult traceRouteResult, final int index) {
        paseStr1(traceRouteResult.getMessage(), ip);
        List<IpAddEntity> ipAddEntityList = new ArrayList<>();
        if (!ipList.isEmpty()) {
//            String s = ipList.get(ipList.size() - 1);
//            int i1 = s.lastIndexOf(".");
//            String substring = s.substring(0, i1);
//            substring += ".0";
//            ipList.set(ipList.size() - 1, substring);
            for (int i = 0; i < ipList.size(); i++) {
                IpAddEntity ipAddEntity = new IpAddEntity();
                ipAddEntity.setTask_id(object.getTask_id());
                ipAddEntity.setSort_asc(i + 1);
                ipAddEntity.setSort_desc(ipList.size() - i);
                ipAddEntity.setOriginal_ip(object.getClient_ip());
                ipAddEntity.setTarget_ip(ip);
                ipAddEntity.setTrace_ip(ipList.get(i));
                ipAddEntity.setCreate_time((int) System.currentTimeMillis());
                ipAddEntityList.add(ipAddEntity);
            }
        }
        final Map<String, Object> map = new HashMap<>();
        map.put("ip_type", networkType);
        map.put("ip", object.getClient_ip());
        map.put("is_vpn", vpnUsed ? 1 : 2);
        map.put("task_id", object.getTask_id());
        Gson gson = new Gson();
        map.put("trace", !ipAddEntityList.isEmpty() ? gson.toJson(ipAddEntityList) : "");
        map.put("longitude", location != null ? location.getLongitude() + "" : "");
        map.put("latitude", location != null ? location.getLatitude() + "" : "");
        if (location != null) {
            List<Address> addList = null;
            Geocoder geocoder = new Geocoder(mContext);
            try {
                addList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addList != null && addList.size() > 0) {
                    for (int i = 0; i < addList.size(); i++) {
                        Address ad = addList.get(i);
                        area = ad.getAddressLine(0);
//                        cityName += ad.getCountryName() + " " + ad.getLocality();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        map.put("area", area);
        String key = CBCUtil.encrypt(JSON.toJSONString(map));
        JavaHttpRequest.getIPInfoAdd(key, new HttpCallback<InitEntity>() {
            @Override
            public void onSuccess(BaseResponse<InitEntity> response) {
                getDataip(index + 1);
            }

            @Override
            public void onFailed(int code, String message) {
                getDataip(index + 1);
            }
        });
    }

    private void paseStr1(String str, String item) {
        String result = "";
        String regEx = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        int i = 0;
        while (m.find()) {
            i++;
            result = m.group();
            if (!result.isEmpty() && !result.equals(item)) {
                ipList.add(result);
            }
            Log.e("---ip", "getIP: " + i + " " + result);
//break;   加break则提取string中的一个IP
        }
    }


    @Override
    public int getIp_type() {
        //wifi 2 流量1
        ConnectivityManager connectMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        return networkType = info != null && info.getType() == ConnectivityManager.TYPE_WIFI ? 2 : 1;
    }

    @Override
    public void onDestroy() {

    }


    /**
     * 检测是否正在使用VPN，如果在使用返回true,反之返回flase
     */
    @Override
    public boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (Object o : Collections.list(niList)) {
                    NetworkInterface intf = (NetworkInterface) o;
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
//                    LogUtils.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private Location getLastKnownLocation() {
        //获取地理位置管理器
        LocationManager mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO:去请求权限后再获取
            return null;
        }
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
// 在一些手机5.0(api21)获取为空后，采用下面去兼容获取。
        if (bestLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(false);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = mLocationManager.getBestProvider(criteria, true);
            if (!TextUtils.isEmpty(provider)) {
                bestLocation = mLocationManager.getLastKnownLocation(provider);
            }
        }
        return bestLocation;
    }

}
