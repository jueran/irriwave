package com.salted.core.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 说明：加密的工具类
 * 作者：杨健
 * 时间：2017/7/24.
 */
public final class Encryption {
    private Encryption() {
    }

    /**
     * encode string data
     *
     * @param data
     * @param algorithm MD5  SHA1
     */
    public static String encode(String data, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(data.getBytes());
            byte md[] = digest.digest();
            StringBuilder sb = new StringBuilder(md.length * 2);
            for (int i = 0; i < md.length; i++) {
                sb.append(Integer.toHexString((md[i] & 0xf0) >>> 4));
                sb.append(Integer.toHexString(md[i] & 0x0f));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
