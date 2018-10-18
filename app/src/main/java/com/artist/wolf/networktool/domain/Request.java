package com.artist.wolf.networktool.domain;

import java.io.File;
import java.util.HashMap;

public class Request {

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public enum RestfulAPI {
        GET,
        POST,
        PUT,
        DELETE,
    }

    public static final int DEFAULT_TIMEOUT = 30 * 1000;

    private RestfulAPI restfulAPI;
    private String apiUrl;
    private String body;
    private File file;
    private int timeout;

    private Request() {
        this.restfulAPI = RestfulAPI.GET;
        this.apiUrl = "";
        this.body = "";
        this.file = null;
        this.timeout = DEFAULT_TIMEOUT;
    }

    public Request(RestfulAPI restfulAPI, String apiUrl) {
        this();
        this.restfulAPI = restfulAPI;
        this.apiUrl = apiUrl;
    }

    public Request(RestfulAPI restfulAPI, String apiUrl, int timeout) {
        this(restfulAPI, apiUrl);
        this.timeout = timeout;
    }

    public RestfulAPI getRestfulAPI() {
        return restfulAPI;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getBody() {
        return body;
    }

    public File getFile() {
        return file;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
