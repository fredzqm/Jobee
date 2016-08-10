package com.fredzqm.jobee.notification;

import android.content.Context;
import android.util.Log;

import com.fredzqm.jobee.R;
import com.loopj.android.http.*;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class RequestSender extends TextHttpResponseHandler {
    private static final String TAG = "RequestSender";
    private static final String URL = "https://fcm.googleapis.com/fcm/send";

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
     * @param text
     */
    public static void notifyApp(String toToken, String title, String text) {
        RequestParams params = new RequestParams();

        Map<String, String> notification = new HashMap<String, String>();

        Map<String, String> data = new HashMap<String, String>();
        data.put("dtitle", title);
        data.put("dtext", text);
        data.put("dclick_action", "NOTIFICATION_ACTION");

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