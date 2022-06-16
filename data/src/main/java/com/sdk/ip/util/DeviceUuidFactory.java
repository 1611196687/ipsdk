package com.sdk.ip.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import net.jcip.annotations.ThreadSafe;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * UUID 获取
 */
@ThreadSafe
public class DeviceUuidFactory {
    public DeviceUuidFactory() {
        //Do nothing
    }

    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static UUID uuid;

    public static synchronized UUID getInstance(Context context) {
        if (uuid == null) {
            final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
            final String id = prefs.getString(PREFS_DEVICE_ID, null);
            if (id != null) {
                uuid = UUID.fromString(id);
            } else {
                @SuppressLint("HardwareIds") final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                try {
                    if (!"9774d56d682e549c".equals(androidId)) {
                        uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                    } else {
                        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                        uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                    }
                } catch (UnsupportedEncodingException | SecurityException e) {
                    throw new RuntimeException(e);
                }
                prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).apply();
            }
        }
        return uuid;
    }


}
