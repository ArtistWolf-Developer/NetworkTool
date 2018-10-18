package com.artist.wolf.networktool;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.artist.wolf.networktool.domain.Request;
import com.artist.wolf.networktool.domain.Response;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import static android.provider.Telephony.Mms.Part.CHARSET;
import static com.artist.wolf.networktool.domain.Response.STATUS_CANCEL;
import static com.artist.wolf.networktool.domain.Response.STATUS_FINISH;
import static com.artist.wolf.networktool.domain.Response.STATUS_PROCESSING;
import static com.artist.wolf.networktool.domain.Response.STATUS_START;

public class NetworkTask extends AsyncTask<String, Integer, Response> {

    private static final String TAG = "NetworkTask";

    private NetworkResponseCallback networkResponseCallback;
    private NetworkRequestStatusListener networkRequestStatusListener;
    private Request request;
    private HashMap<String, String> headers;

    private NetworkTask() {
    }

    public NetworkTask(NetworkResponseCallback networkResponseCallback,
                       NetworkRequestStatusListener networkRequestStatusListener,
                       Request request,
                       HashMap<String, String> headers) {
        this.networkResponseCallback = networkResponseCallback;
        this.networkRequestStatusListener = networkRequestStatusListener;
        this.request = request;
        this.headers = headers;
    }

    /**
     * 執行前
     */
    @Override
    protected void onPreExecute() {
        Log.d(TAG, "[onPreExecute]");
        super.onPreExecute();
        if (this.networkRequestStatusListener != null) {
            this.networkRequestStatusListener.onStatusChange(STATUS_START, true);
        }
    }

    /**
     * 執行完畢
     *
     * @param response
     */
    @Override
    protected void onPostExecute(Response response) {
        Log.d(TAG, "[onPostExecute]");
        super.onPostExecute(response);
        if (this.networkRequestStatusListener != null) {
            this.networkRequestStatusListener.onStatusChange(STATUS_FINISH, true);
        }
    }

    /**
     * 執行進度
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(TAG, "[onProgressUpdate]");
        super.onProgressUpdate(values);
        if (this.networkRequestStatusListener != null) {
            this.networkRequestStatusListener.onProgressUpdate(values[0]);
        }
    }

    @Override
    protected void onCancelled(Response s) {
        Log.d(TAG, "[onCancelled]");
        super.onCancelled(s);
        if (this.networkRequestStatusListener != null) {
            this.networkRequestStatusListener.onStatusChange(STATUS_CANCEL, true);
        }
    }

    @Override
    protected void onCancelled() {
        Log.d(TAG, "[onCancelled]");
        super.onCancelled();
        if (this.networkRequestStatusListener != null) {
            this.networkRequestStatusListener.onStatusChange(STATUS_CANCEL, true);
        }
    }

    /**
     * 執行中
     *
     * @param strings
     * @return
     */
    @Override
    protected Response doInBackground(String... strings) {
        Log.d(TAG, "[doInBackground]");
        if (this.networkRequestStatusListener != null) {
            this.networkRequestStatusListener.onStatusChange(STATUS_PROCESSING, true);
        }

        HttpsURLConnection httpsURLConnection = getHttpsURLConnection(strings[0]);

        Response response = new Response(this.request.getApiUrl());
        InputStream inputStream;

        if (httpsURLConnection == null) {
            String errorMessage = "Create HttpsUrlConnection faile";
            Log.e(TAG, "[doInBackground] " + errorMessage);
            response.setSuccess(false);
            inputStream = new ByteArrayInputStream(errorMessage.getBytes(StandardCharsets.UTF_8));
        } else {
            if (!TextUtils.isEmpty(this.request.getBody())) {
                requestForBody(httpsURLConnection);
            }

            if (this.request.getFile() != null) {
                requestForFile(httpsURLConnection);
            }

            try {
                int status = httpsURLConnection.getResponseCode();
                response.setResponseStatus(status);
                switch (status) {
                    case 200:
                    case 201:
                        response.setSuccess(true);
                        break;
                    case 401:
                    case 400:
                        response.setSuccess(false);
                        break;
                }

                inputStream = httpsURLConnection.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                response.setSuccess(false);
                inputStream = new ByteArrayInputStream(e.getMessage().toString().getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                response.setSuccess(false);
                inputStream = new ByteArrayInputStream(e.getMessage().toString().getBytes(StandardCharsets.UTF_8));
            }
        }

        response.setResponse(inputStream);
        this.networkResponseCallback.onResponse(response);
        httpsURLConnection.disconnect();

        return response;
    }

    private HttpsURLConnection getHttpsURLConnection(String urlDomain) {
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(urlDomain);

            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod(this.request.getRestfulAPI().toString());
            httpsURLConnection.setConnectTimeout(this.request.getTimeout());
            httpsURLConnection.setReadTimeout(this.request.getTimeout());

            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                httpsURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            if (this.request.getRestfulAPI().equals(Request.RestfulAPI.PUT)
                    || this.request.getRestfulAPI().equals(Request.RestfulAPI.POST)) {
                httpsURLConnection.setDoOutput(true);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpsURLConnection;
    }

    private void requestForBody(HttpsURLConnection httpsURLConnection) {
        try {
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(this.request.getBody());
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestForFile(HttpsURLConnection httpsURLConnection) {
        String boundary = UUID.randomUUID().toString();  //边界标识   随机生成
        String prefix = "--";
        String lineEnd = "\r\n";
        String contentType = "multipart/form-data";   //内容类型

        try {
            httpsURLConnection.setDoInput(true);  //允许输入流
            httpsURLConnection.setDoOutput(true); //允许输出流
            httpsURLConnection.setUseCaches(false);  //不允许使用缓存
            httpsURLConnection.setRequestMethod("POST");  //请求方式
            httpsURLConnection.setRequestProperty("Charset", "utf-8");  //设置编码
            httpsURLConnection.setRequestProperty("connection", "keep-alive");
            httpsURLConnection.setRequestProperty("Content-Type", contentType + ";boundary=" + boundary);

            OutputStream outputSteam = httpsURLConnection.getOutputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(outputSteam);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(prefix);
            stringBuffer.append(boundary);
            stringBuffer.append(lineEnd);

            stringBuffer.append("Content-Disposition: form-data; name=\"dev_img\"; filename=\"" + this.request.getFile().getName() + "\"" + lineEnd);
            stringBuffer.append("Content-Type: application/octet-stream; charset=" + CHARSET + lineEnd);
            stringBuffer.append(lineEnd);

            dataOutputStream.write(stringBuffer.toString().getBytes());
            InputStream is = new FileInputStream(this.request.getFile());
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dataOutputStream.write(bytes, 0, len);
            }
            is.close();
            dataOutputStream.write(lineEnd.getBytes());
            byte[] end_data = (prefix + boundary + prefix + lineEnd).getBytes();
            dataOutputStream.write(end_data);
            dataOutputStream.flush();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
