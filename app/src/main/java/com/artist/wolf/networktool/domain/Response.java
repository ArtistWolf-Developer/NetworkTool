package com.artist.wolf.networktool.domain;

import java.io.InputStream;

public class Response {

    public static final int STATUS_START = -1;
    public static final int STATUS_FINISH = 0;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_CANCEL = 3;

    protected String apiUrl;
    protected int responseStatus;
    protected InputStream response;
    protected boolean isSuccess;

    public Response() {
        this.apiUrl = "";
        this.responseStatus = -1;
        this.response = null;
        this.isSuccess = false;
    }

    public Response(String apiUrl) {
        this();
        this.apiUrl = apiUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public InputStream getResponse() {
        return response;
    }

    public void setResponse(InputStream response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
