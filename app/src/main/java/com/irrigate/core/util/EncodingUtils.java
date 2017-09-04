package com.irrigate.core.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by yuan on 16/7/9.
 */
public final class EncodingUtils {

    private EncodingUtils() {

    }

    public static String getString(
            final byte[] data,
            int offset,
            int length,
            String charset) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }
        try {
            return new String(data, offset, length, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(data, offset, length);
        }
    }

    /**
     * Converts the byte array of HTTP content characters to a string. If
     * the specified charset is not supported, default system encoding
     * is used.
     *
     * @param data    the byte array to be encoded
     * @param charset the desired character encoding
     * @return The result of the conversion.
     */
    public static String getString(final byte[] data, final String charset) {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        return getString(data, 0, data.length, charset);
    }
}
