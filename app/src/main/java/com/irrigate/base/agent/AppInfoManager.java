package com.irrigate.base.agent;

import android.text.TextUtils;

import com.irrigate.base.utils.SharedPreferenceUtil;
import com.irrigate.constant.SharedPrefKeys;

/**
 * Created by ldf on 17/8/2.
 */

public final class AppInfoManager {
    //饿汉模式
    private static AppInfoManager instance = new AppInfoManager();

    private AppInfoManager() {
    }

    public static AppInfoManager getInstance() {
        synchronized (AppInfoManager.class) {
            if (instance == null) {
                instance = new AppInfoManager();
            }
        }
        return instance;
    }

    /**
     * 得到设置SECRET_ID
     *
     * @return String secretId
     */
    public String getSecretId() {
        String clientID = (String) SharedPreferenceUtil.getObject(
                SharedPrefKeys.SECRET_ID, String.class);
        return clientID;
    }

    /**
     * 设置SECRET_ID
     *
     * @param secretId
     * @return void
     */
    public void setSecretId(String secretId) {
        if (!TextUtils.isEmpty(secretId)) {
            SharedPreferenceUtil.put(SharedPrefKeys.SECRET_ID, secretId);
        }
    }

    /**
     * 得到CLIENT_ID
     *
     * @return String clientId
     */
    public String getClientId() {
        String clientID = (String) SharedPreferenceUtil.getObject(
                SharedPrefKeys.CLIENT_ID, String.class);
        return clientID;
    }

    /**
     * 设置CLIENT_ID
     *
     * @param clientId
     * @return void
     */
    public void setClientId(String clientId) {
        if (!TextUtils.isEmpty(clientId)) {
            SharedPreferenceUtil.put(SharedPrefKeys.CLIENT_ID, clientId);
        }
    }
}
