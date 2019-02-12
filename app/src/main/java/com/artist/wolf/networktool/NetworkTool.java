package com.artist.wolf.networktool;

import android.os.AsyncTask;
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
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }

        if (this.cacheNetworkTasks == null) {
            this.cacheNetworkTasks = new HashMap<>();
        }
        return this;
    }

    public NetworkTool init(String domain, int timeout) {
        init(domain);
        this.timeout = timeout;
        return this;
    }

    public NetworkTool init(String domain, int timeout, HostnameVerifier hostnameVerifier, String sslProtocol, X509TrustManager x509TrustManager) {
        init(domain, timeout);
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

    public void get(boolean isMulti, String aipUrl, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = null;
        Request request = new Request(Request.RestfulAPI.GET, aipUrl);
        connect(isMulti, aipUrl, request);
    }

    public void get(boolean isMulti, String aipUrl, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = networkRequestStatusListener;
        Request request = new Request(Request.RestfulAPI.GET, aipUrl);
        connect(isMulti, aipUrl, request);
    }

    public void post(boolean isMulti, String aipUrl, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = null;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        connect(isMulti, aipUrl, request);
    }

    public void post(boolean isMulti, String aipUrl, String body, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = null;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        request.setBody(body);
        connect(isMulti, aipUrl, request);
    }

    public void post(boolean isMulti, String aipUrl, File file, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = null;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        request.setFile(file);
        connect(isMulti, aipUrl, request);
    }

    public void post(boolean isMulti, String aipUrl, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = networkRequestStatusListener;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        connect(isMulti, aipUrl, request);
    }

    public void post(boolean isMulti, String aipUrl, String body, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = networkRequestStatusListener;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        request.setBody(body);
        connect(isMulti, aipUrl, request);
    }

    public void post(boolean isMulti, String aipUrl, File file, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = networkRequestStatusListener;
        Request request = new Request(Request.RestfulAPI.POST, aipUrl);
        request.setFile(file);
        connect(isMulti, aipUrl, request);
    }

    public void put(boolean isMulti, String aipUrl, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = null;
        Request request = new Request(Request.RestfulAPI.PUT, aipUrl);
        connect(isMulti, aipUrl, request);
    }

    public void put(boolean isMulti, String aipUrl, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = networkRequestStatusListener;
        Request request = new Request(Request.RestfulAPI.PUT, aipUrl);
        connect(isMulti, aipUrl, request);
    }

    public void put(boolean isMulti, String aipUrl, String body, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = null;
        Request request = new Request(Request.RestfulAPI.PUT, aipUrl);
        request.setBody(body);
        connect(isMulti, aipUrl, request);
    }

    public void put(boolean isMulti, String aipUrl, String body, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = networkRequestStatusListener;
        this.networkRequestStatusListener = null;
        Request request = new Request(Request.RestfulAPI.PUT, aipUrl);
        request.setBody(body);
        connect(isMulti, aipUrl, request);
    }

    public void delete(boolean isMulti, String aipUrl, NetworkResponseCallback networkResponseCallback) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = null;
        Request request = new Request(Request.RestfulAPI.DELETE, aipUrl);
        connect(isMulti, aipUrl, request);
    }

    public void delete(boolean isMulti, String aipUrl, NetworkResponseCallback networkResponseCallback, NetworkRequestStatusListener networkRequestStatusListener) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = networkRequestStatusListener;
        Request request = new Request(Request.RestfulAPI.DELETE, aipUrl);
        connect(isMulti, aipUrl, request);
    }

    private void connect(boolean isMulti, String aipUrl, Request request) {
        request.setTimeout(this.timeout);

        NetworkTask networkTask = new NetworkTask(this,
                this,
                request,
                this.headers);

        this.cacheNetworkTasks.put(aipUrl, networkTask);

        if (isMulti) {
            networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.domain + aipUrl);
        } else {
            networkTask.execute(this.domain + aipUrl);
        }
    }
}
