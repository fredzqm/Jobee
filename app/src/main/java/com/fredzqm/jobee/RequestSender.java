package com.fredzqm.jobee;

import android.content.Context;

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

public class RequestSender extends JsonHttpResponseHandler {
    private static final String URL = "https://fcm.googleapis.com/fcm/send";

    private static AsyncHttpClient client;

    static {
        client = new AsyncHttpClient();
//        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//        client.setSSLSocketFactory(sslsocketfactory);
    }
    private final Context mContext;

    public RequestSender(Context context){
        mContext = context;
    }

    public void notifyApp(String toToken , String title, String text) {
        Header[] headers = new Header[1];
        headers[0] = new BasicHeader("Authorization", "key="+ mContext.getString(R.string.server_key));
        RequestParams params = new RequestParams();
        params.add("to", toToken);
        Map<String, String> map = new HashMap<String, String>();
        map.put("title",title);
        map.put("text", text);
        params.put("notification", map);

        client.post(URL, params, this);
//        client.post(mContext, URL, headers, params, "application/json", this);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // If the response is JSONObject instead of expected JSONArray
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
        // Pull out the first event on the public timeline
        JSONObject firstEvent = null;
        String tweetText = null;
        try {
            firstEvent = (JSONObject) timeline.get(0);
            tweetText = firstEvent.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Do something with the response
        System.out.println(tweetText);
    }


    @Override
    public void onStart() {
        // called before request is started
    }


    @Override
    public void onRetry(int retryNo) {
        // called when request is retried
    }
}