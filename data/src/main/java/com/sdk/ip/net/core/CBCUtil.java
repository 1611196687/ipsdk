package com.sdk.ip.net.core;


import android.annotation.SuppressLint;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CBCUtil {


    private static String key = "";
    private static String iv1 = "";

    public static void setKey(String keys) {
        key = keys;
        iv1 = keys;
    }

    @SuppressLint("NewApi")
    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(iv1.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: " + Base64.encodeToString(encrypted,Base64.DEFAULT));

            return Base64.encodeToString(encrypted,Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @SuppressLint("NewApi")
    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(iv1.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted,Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
