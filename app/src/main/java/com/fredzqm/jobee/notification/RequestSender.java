package com.fredzqm.jobee.notification;

import android.content.Context;
import android.util.Log;

import com.fredzqm.jobee.R;
import com.loopj.android.http.*;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class RequestSender extends TextHttpResponseHandler {
    public static final String TAG = "RequestSender";
    public static final String URL = "https://fcm.googleapis.com/fcm/send";
    public static final String TITLE = "TITLE";
    public static final String BODY = "BODY";


    private static AsyncHttpClient client;
    private static ResponseHandlerInterface requestSender;

    static {
        client = new AsyncHttpClient();
        client.addHeader("Authorization", " key=AIzaSyC6lpoN9ekbvLjY5AtAOOHZmvLcSscYdes");
        requestSender = new RequestSender();
    }

    /** Example here
     * String token = FirebaseInstanceId.getInstance().getToken();
     * Log.d(TAG, token);
     * RequestSender requestSender = new RequestSender(LoginActivity.this);
     * requestSender.notifyApp(token, "Portugal vs. Denmark", "5 to 1");
     *
     * @param toToken
     * @param title
     * @param body
     */
    public static void notifyApp(String toToken, String title, String body) {
        RequestParams params = new RequestParams();
        Map<String, String> data = new HashMap<String, String>();
        data.put(TITLE, title);
        data.put(BODY, body);
        params.put("to", toToken);
        params.put("data", data);
        client.post(URL, params, requestSender);
    }


    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d(TAG, "statusCode: " + statusCode + "\n" + responseString);
        throw new RuntimeException(throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        Log.d(TAG, "statusCode: " + statusCode + "\n" + responseString);
    }


}