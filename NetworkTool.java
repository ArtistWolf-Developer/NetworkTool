package com.artist.wolf.networktool;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.artist.wolf.networktool.domain.Request;
import com.artist.wolf.networktool.domain.Response;
import com.artist.wolf.networktool.domain.SecurityProtocolConfig;

import java.io.File;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

public class NetworkTool extends NetworkResponseCallback implements NetworkRequestStatusListener {

    private static final String TAG = "[NetworkTool]";
    private static NetworkTool networkTool = new NetworkTool();

    private String domain;
    private int timeout;
    private HashMap<String, String> headers;
    private NetworkResponseCallback networkResponseCallback;
    private NetworkRequestStatusListener networkRequestStatusListener;

    private HashMap<String, NetworkTask> cacheNetworkTasks;

    private NetworkTool() {
    }

    public static NetworkTool getInstance() {
        return networkTool;
    }

    @Override
    public void onStatusChange(int status, boolean isSuccess) {
        Log.d(TAG, "[onStatus]");
        if (this.networkRequestStatusListener != null) {
            this.networkRequestStatusListener.onStatusChange(status, isSuccess);
        }
    }

    @Override
    public void onProgressUpdate(int progress) {
        Log.d(TAG, "[onProgressStatusChange]");
        if (this.networkRequestStatusListener != null) {
            this.networkRequestStatusListener.onProgressUpdate(progress);
        }
    }

    @Override
    public void onResponse(Response response) {
        Log.d(TAG, "[onResponse]");
        if (this.networkResponseCallback != null) {
            this.networkResponseCallback.onResponse(response);
        }

        if (this.cacheNetworkTasks != null) {
            this.cacheNetworkTasks.remove(response.getApiUrl());
        }
    }

    public NetworkTool init(String domain) {
        this.domain = domain;
        this.timeout = Request.DEFAULT_TIMEOUT;
        this.headers = new HashMap<>();
        this.cacheNetworkTasks = new HashMap<>();
        return this;
    }

    public NetworkTool init(String domain, int timeout) {
        init(domain);
        this.domain = domain;
        this.timeout = timeout;
        this.headers = new HashMap<>();
        this.cacheNetworkTasks = new HashMap<>();
        return this;
    }

    public NetworkTool init(String domain, int timeout, HostnameVerifier hostnameVerifier, String sslProtocol, X509TrustManager x509TrustManager) {
        this.domain = domain;
        this.timeout = timeout;
        this.headers = new HashMap<>();
        this.cacheNetworkTasks = new HashMap<>();
        SecurityProtocolConfig.getInsntance().init(hostnameVerifier, sslProtocol, x509TrustManager);
        return this;
    }

    public NetworkTool setHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }

        this.headers.put(key, value);

        return this;
    }

    public void get(String aipUrl, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        Request request = new Request(Request.RestfulAPI.GET, aipUrl);
        connect(aipUrl, request);
    }

    public void get(String aipUrl, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkRequestStatusListener = networkRequestStatusListener;
        get(aipUrl, networkResponseCallback);
    }

    public void post(String aipUrl, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        connect(aipUrl, request);
    }

    public void post(String aipUrl, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkRequestStatusListener = networkRequestStatusListener;
        post(aipUrl, networkResponseCallback);
    }

    public void post(String aipUrl, String body, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkRequestStatusListener = networkRequestStatusListener;
        this.networkResponseCallback = networkResponseCallback;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        request.setBody(body);
        connect(aipUrl, request);
    }

    public void post(String aipUrl, File file, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkRequestStatusListener = networkRequestStatusListener;
        this.networkResponseCallback = networkResponseCallback;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        request.setFile(file);
        connect(aipUrl, request);
    }

    public void put(String aipUrl, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        Request request = new Request(Request.RestfulAPI.PUT, aipUrl);
        connect(aipUrl, request);
    }

    public void put(String aipUrl, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkRequestStatusListener = networkRequestStatusListener;
        put(aipUrl, networkResponseCallback);
    }

    public void put(String aipUrl, String body, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        Request request = new Request(Request.RestfulAPI.PUT, aipUrl);
        request.setBody(body);
        connect(aipUrl, request);
    }

    public void delete(String aipUrl, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        Request request = new Request(Request.RestfulAPI.DELETE, aipUrl);
        connect(aipUrl, request);
    }

    public void delete(String aipUrl, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkRequestStatusListener = networkRequestStatusListener;
        delete(aipUrl, networkResponseCallback);
    }

    private void connect(String aipUrl, Request request) {
        request.setTimeout(this.timeout);

        NetworkTask networkTask = new NetworkTask(this,
                this,
                request,
                this.headers);

        this.cacheNetworkTasks.put(aipUrl, networkTask);
        networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.domain + aipUrl);
    }
}
