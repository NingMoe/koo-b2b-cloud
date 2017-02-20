package com.koolearn.cloud.operation.entity;


import com.koolearn.security.client.entity.SecurityUser;

import java.io.Serializable;

public class OperationUser implements Serializable {
    public static final String SECURITY_CAS_APPID = "cas_appId";//权限系统appid配置名
    public static final String SECURITY_USERLIST_CACHE_KEY = "cloud_security_userlist";
    private SecurityUser securityUser;// 权限用户
    private int userId;//取自securityUser Id
    private boolean isExistUser = false;

    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public void setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
    }

    public boolean isExistUser() {
        if (this.getSecurityUser() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void setIsExistUser(boolean isExistUser) {
        this.isExistUser = isExistUser;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
