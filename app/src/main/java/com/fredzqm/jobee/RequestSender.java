package com.fredzqm.jobee;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class RequestSender extends TextHttpResponseHandler {
    private static final String TAG = "RequestSender";
    private static final String URL = "https://fcm.googleapis.com/fcm/send";

    private AsyncHttpClient client;

    private final Context mContext;

    public RequestSender(Context context){
        mContext = context;
        client = new AsyncHttpClient();
        client.addHeader("Authorization", " key="+ mContext.getString(R.string.server_key));
    }

    public void notifyApp(String toToken , String title, String text) {
        RequestParams params = new RequestParams();
        params.add("to", toToken);
        Map<String, String> map = new HashMap<String, String>();
        map.put("title",title);
        map.put("text", text);
        params.put("notification", map);

        client.post(URL, params, this);
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