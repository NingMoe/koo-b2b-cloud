package com.koolearn.cloud.util.hexin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gehaisong on 2016/9/2.
 */
public class SHA1Entity implements Serializable {
    private String accessKey;
    private String timestamp;
    private String nonce;
    private String accessSecret;
    private String signature ;
    private Map<String,String> paramMap=new HashMap<String, String>();

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }
}