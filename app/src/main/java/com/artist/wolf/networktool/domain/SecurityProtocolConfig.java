package com.artist.wolf.networktool.domain;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class SecurityProtocolConfig {

    private static SecurityProtocolConfig securityProtocolConfig = new SecurityProtocolConfig();
    private HostnameVerifier hostnameVerifier;
    private SSLSocketFactory sslSocketFactory;

    private SecurityProtocolConfig() {
    }

    public static SecurityProtocolConfig getInsntance() {
        return securityProtocolConfig;
    }

    public SecurityProtocolConfig init(HostnameVerifier hostnameVerifier, String sslProtocol, X509TrustManager x509TrustManager) {
        this.hostnameVerifier = hostnameVerifier;
        try {
            SSLContext sslContext = SSLContext.getInstance(sslProtocol);
            sslContext.init(null, new X509TrustManager[]{x509TrustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return this;
    }
}
