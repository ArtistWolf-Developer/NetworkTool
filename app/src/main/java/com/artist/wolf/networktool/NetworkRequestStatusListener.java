package com.artist.wolf.networktool;

public interface NetworkRequestStatusListener {

    void onStatusChange(int status, boolean isSuccess);

    void onProgressUpdate(int progress);

}
